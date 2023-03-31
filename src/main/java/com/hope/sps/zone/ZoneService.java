package com.hope.sps.zone;

import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.zone.space.Space;
import com.hope.sps.zone.space.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;

    private final ZoneDTOMapper zoneDTOMapper;

    private final ZoneRegistrationRequestMapper zoneRegistrationRequestMapper;

    private final ZoneUpdateRequestMapper zoneUpdateRequestMapper;

    private final SpaceRepository spaceRepository;

    public List<ZoneDTO> getAll() {
        List<ZoneDTO> zoneDTOS = zoneRepository.findAll()
                .stream()
                .map(zoneDTOMapper)
                .toList();

        zoneDTOS.forEach(zoneDTO -> {
            zoneDTO.setTotalSpaces(
                    spaceRepository.countSpaceByZoneId(zoneDTO.getZoneId()));
            zoneDTO.setAvailableSpaces(
                    spaceRepository.countByStateIsAndId(Space.State.AVAILABLE, zoneDTO.getZoneId()));
        });

        return zoneDTOS;
    }

    public Long registerZone(ZoneRegistrationRequest request) {
        Zone toRegisterZone = zoneRegistrationRequestMapper.apply(request);

        toRegisterZone.setSpaces(
                IntStream
                        .rangeClosed(1, toRegisterZone.getNumberOfSpaces())
                        .mapToObj(Space::new)
                        .collect(Collectors.toSet()));

        return zoneRepository.save(toRegisterZone).getId();
    }

    public void updateZone(Long zoneId, ZoneUpdateRequest request) {
        Zone toUpdateZone = zoneUpdateRequestMapper.apply(request);

        Zone oldZoneInfo = zoneRepository
                .findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "could not found zone with id: %d".formatted(zoneId)));

        if (!toUpdateZone.getNumberOfSpaces().equals(oldZoneInfo.getNumberOfSpaces())) {
            spaceRepository.removeAllByZoneId(zoneId);

            toUpdateZone.setSpaces(
                    IntStream
                            .rangeClosed(1, toUpdateZone.getNumberOfSpaces())
                            .mapToObj(Space::new)
                            .collect(Collectors.toSet()));
        }
        zoneRepository.save(toUpdateZone);
    }

    public void removeZone(Long zoneId) {
        zoneRepository.deleteById(zoneId);
    }

}
