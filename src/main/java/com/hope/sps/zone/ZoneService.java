package com.hope.sps.zone;

import com.hope.sps.exception.DuplicateResourceException;
import com.hope.sps.exception.InvalidResourceProvidedException;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.zone.space.Space;
import com.hope.sps.zone.space.SpaceDTO;
import com.hope.sps.zone.space.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;

    private final SpaceRepository spaceRepository;

    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<ZoneDTO> getAll() {
        final List<Zone> zones = zoneRepository.findAll();

        return zones.stream().map(zone -> {
            final var zoneDTO = mapper.map(zone, ZoneDTO.class);
            zoneDTO.setAvailableSpaces(countAvailableSpace(zone));
            zoneDTO.setSpaceList(zone.getSpaces().stream().map(space -> mapper.map(space, SpaceDTO.class)).toList());
            return zoneDTO;
        }).toList();
    }


    @Transactional(readOnly = true)
    public ZoneDTO getZoneById(final Long id) {
        final Zone zone = zoneRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Zone with this id not found")
                );

        final var zoneDTO = mapper.map(zone, ZoneDTO.class);
        zoneDTO.setAvailableSpaces(countAvailableSpace(zone));
        zoneDTO.setSpaceList(zone.getSpaces().stream().map(space -> mapper.map(space, SpaceDTO.class)).toList());
        return zoneDTO;
    }

    public Long registerZone(final ZoneRegistrationRequest request) {
        if (zoneRepository.existsByTag(request.getTag()))
            throw new DuplicateResourceException("zone with same tag already exists");

        if (request.getStartsAt().after(request.getEndsAt()))
            throw new InvalidResourceProvidedException("Start time cant be before end time");

        final var toRegisterZone = mapper.map(request, Zone.class);
        toRegisterZone.setSpaces(
                IntStream
                        .rangeClosed(1, toRegisterZone.getNumberOfSpaces())
                        .mapToObj(Space::new)
                        .collect(Collectors.toSet()));
        return zoneRepository.save(toRegisterZone).getId();
    }

    @Transactional
    public void updateZone(final Long zoneId, final ZoneUpdateRequest request) {
        final var zone = zoneRepository
                .findById(zoneId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("could not found zone with id: %d".formatted(zoneId))
                );

        if (request.getStartsAt().after(request.getEndsAt()))
            throw new InvalidResourceProvidedException("Start time cant be before end time");

        final Integer numberOfSpaces = request.getNumberOfSpaces();
        if (numberOfSpaces != null && !zone.getNumberOfSpaces().equals(numberOfSpaces)) {
            spaceRepository.removeAllByZoneId(zoneId);
            zone.setSpaces(
                    IntStream
                            .rangeClosed(1, numberOfSpaces)
                            .mapToObj(Space::new)
                            .collect(Collectors.toSet()));

            zone.setNumberOfSpaces(numberOfSpaces);
        }

        zone.setFee(request.getFee());
        zone.setTitle(request.getTitle());
        zone.setStartsAt(request.getStartsAt());
        zone.setEndsAt(request.getEndsAt());

        zoneRepository.save(zone);
    }

    public void removeZone(final Long zoneId) {
        zoneRepository.deleteById(zoneId);
    }

    private Long countAvailableSpace(final Zone zone) {
        return zone.getSpaces()
                .stream()
                .filter(Space::isAvailable)
                .count();
    }
}
