package com.hope.sps.zone.space;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/space")
public class SpaceController {

    private final SpaceService spaceService;

    @GetMapping("check")
    public ResponseEntity<SpaceAvailabilityResponse> checkSpaceAvailability(@RequestBody SpaceAvailabilityRequest request) {

        var availabilityResp = spaceService.checkAvailability(request);

        return ResponseEntity.ok(availabilityResp);
    }
}
