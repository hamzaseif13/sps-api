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

import java.sql.Time;
import java.util.List;
import java.util.Set;
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
        // foreach zone maps it to zoneDTO, set AvailableSpaces number, and map their spaces to spaceDTO
        return zoneRepository.findAll()
                .stream()
                .map(zone -> {
                    final var zoneDTO = mapper.map(zone, ZoneDTO.class);
                    zoneDTO.setAvailableSpaces(countAvailableSpace(zone));
                    zoneDTO.setSpaceList(getSpaceListDTO(zone));
                    return zoneDTO;
                })
                .toList();
    }


    @Transactional(readOnly = true)
    public ZoneDTO getZoneById(final Long id) {
        final var zone = getZoneByIdFromDB(id);

        final var zoneDTO = mapper.map(zone, ZoneDTO.class);
        zoneDTO.setAvailableSpaces(countAvailableSpace(zone));
        zoneDTO.setSpaceList(getSpaceListDTO(zone));
        return zoneDTO;
    }

    public Long registerZone(final ZoneRegistrationRequest request) {

        // there is already zone with same tag?
        throwExceptionIfZoneAlreadyExistsSameTag(request.getTag());

        // is starts-at time before ends-at time?
        throwExceptionIfZoneActiveTimeIsInvalid(request.getStartsAt(), request.getEndsAt());

        final var toRegisterZone = mapper.map(request, Zone.class);

        // generate sequential spaces with its number starting from 1 to numberOfSpaces
        toRegisterZone.setSpaces(getSequentialNumberedSpaces(request.getNumberOfSpaces()));

        return zoneRepository.save(toRegisterZone).getId();
    }

    @Transactional
    public void updateZone(final Long zoneId, final ZoneUpdateRequest request) {

        final var zone = getZoneByIdFromDB(zoneId);

        throwExceptionIfZoneActiveTimeIsInvalid(request.getStartsAt(), request.getEndsAt());

        // if the number of spaces is updated, generate spaces with new number
        if (!zone.getNumberOfSpaces().equals(request.getNumberOfSpaces())) {
            spaceRepository.removeAllByZoneId(zoneId);
            zone.setSpaces(getSequentialNumberedSpaces(request.getNumberOfSpaces()));
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

    /**********HELPER_METHODS*************/
    private Long countAvailableSpace(final Zone zone) {
        return zone.getSpaces()
                .stream()
                .filter(Space::isAvailable)
                .count();
    }

    private List<SpaceDTO> getSpaceListDTO(final Zone zone) {
        return zone.getSpaces()
                .stream()
                .map(space -> mapper.map(space, SpaceDTO.class))
                .toList();
    }

    private Zone getZoneByIdFromDB(final Long zoneId) {
        return zoneRepository.findById(zoneId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Zone with this id not found")
                );
    }

    private void throwExceptionIfZoneAlreadyExistsSameTag(final String zoneTag) {
        if (zoneRepository.existsByTag(zoneTag))
            throw new DuplicateResourceException("zone with same tag already exists");
    }

    private void throwExceptionIfZoneActiveTimeIsInvalid(final Time startsAt, final Time endsAt) {
        if (startsAt.after(endsAt))
            throw new InvalidResourceProvidedException("Start time cant be before end time");
    }

    private Set<Space> getSequentialNumberedSpaces(final int spacesMaxNumber) {
        return IntStream
                .rangeClosed(1, spacesMaxNumber)
                .mapToObj(Space::new)
                .collect(Collectors.toSet());
    }
}
