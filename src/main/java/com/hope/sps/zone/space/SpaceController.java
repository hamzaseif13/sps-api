package com.hope.sps.zone.space;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/space")
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping("check")
    public ResponseEntity<SpaceAvailabilityResponse> checkSpaceAvailability(@RequestBody SpaceAvailabilityRequest request) {

        var availabilityResp = spaceService.checkAvailability(request);

        return ResponseEntity.ok(availabilityResp);
    }
}
