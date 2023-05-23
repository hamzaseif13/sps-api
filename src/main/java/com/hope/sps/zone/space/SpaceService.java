package com.hope.sps.zone.space;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepository;

    public SpaceAvailabilityResponse checkAvailability(SpaceAvailabilityRequest request) {

        if (spaceRepository.existsByZoneIdAndNumberAndStateIs(request.zoneId(), request.spaceNumber(), Space.State.AVAILABLE)) {
            return new SpaceAvailabilityResponse(true, "space available");
        }

        return new SpaceAvailabilityResponse(false, "space not available");
    }
}
