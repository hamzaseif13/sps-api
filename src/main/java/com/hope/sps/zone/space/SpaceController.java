package com.hope.sps.zone.space;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/space")
@PreAuthorize("hasAuthority('OFFICER')")
public class SpaceController {

    private final SpaceService spaceService;

    @GetMapping("{space_id}")
    public ResponseEntity<OccupiedSpaceDTO> getOccupiedSpaceInformation(
            @PathVariable("space_id") Long spaceId
    ) {

        final var occupiedSpaceDTO = spaceService.getOccupiedSpaceInformation(spaceId);

        return ResponseEntity.ok(occupiedSpaceDTO);
    }


    @PostMapping("check")
    public ResponseEntity<SpaceAvailabilityResponse> checkSpaceAvailability(
            @RequestBody SpaceAvailabilityRequest request
    ) {

        final var availabilityResp = spaceService.checkAvailability(request);

        return ResponseEntity.ok(availabilityResp);
    }
}
