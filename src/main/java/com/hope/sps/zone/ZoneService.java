package com.hope.sps.zone;

import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.zone.space.Space;
import com.hope.sps.zone.space.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepository zoneRepository;

    private final ZoneDTOMapper zoneDTOMapper;

    private final ZoneRegistrationRequestMapper zoneRegistrationRequestMapper;

    private final SpaceRepository spaceRepository;

    public List<ZoneDTO> getAll() {
        return zoneRepository.findAll().stream()
                .map(zoneDTOMapper)
                .toList();
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

    @Transactional
    public void updateZone(Long zoneId, ZoneUpdateRequest request) {

        Zone zone = zoneRepository
                .findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "could not found zone with id: %d".formatted(zoneId)));

        if (request.numberOfSpaces()!=null && !zone.getNumberOfSpaces().equals(request.numberOfSpaces())) {

            spaceRepository.removeAllByZoneId(zoneId);
            zone.setSpaces(
                    IntStream
                            .rangeClosed(1, request.numberOfSpaces())
                            .mapToObj(Space::new)
                            .collect(Collectors.toSet()));
            zone.setNumberOfSpaces(request.numberOfSpaces());
        }
        if(request.fee()!=null){
            zone.setFee(request.fee());
        }
        if(request.title()!=null){
            zone.setTitle(request.title());
        }
        if(request.startsAt()!=null){
            zone.setStartsAt(Time.valueOf(request.startsAt()));
        }
        if(request.endsAt()!=null){
            zone.setEndsAt(Time.valueOf(request.endsAt()));
        }
        zoneRepository.save(zone);
    }

    public void removeZone(Long zoneId) {
        zoneRepository.deleteById(zoneId);
    }

    public ZoneDTO getZoneById(Long id) {
        Zone zone =  zoneRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Zone with this id not found"));
        System.out.println(zone);
        return zoneDTOMapper.apply(zone);
    }
}
