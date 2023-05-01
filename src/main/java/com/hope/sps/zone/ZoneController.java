package com.hope.sps.zone;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/zone")
@PreAuthorize("hasAuthority('ADMIN')")
public class ZoneController {

    private final ZoneService zoneService;

    @GetMapping
    public ResponseEntity<List<ZoneDTO>> getAll() {

        final List<ZoneDTO> allZones = zoneService.getAll();
        return ResponseEntity.ok(allZones);
    }

    @GetMapping("{zoneId}")
    public ResponseEntity<ZoneDTO> getZoneById(@PathVariable("zoneId") Long zoneId) {
        return ResponseEntity.ok(zoneService.getZoneById(zoneId));
    }

    @PostMapping
    public ResponseEntity<Long> registerZone(
            @RequestBody @Valid
            ZoneRegistrationRequest request
    ) {
        final Long zoneId = zoneService.registerZone(request);
        return ResponseEntity.ok(zoneId);
    }

    @PutMapping("{zoneId}")
    public ResponseEntity<Long> updateZone(
            @RequestBody @Valid
            ZoneUpdateRequest request,
            @PathVariable Long zoneId
    ) {

        zoneService.updateZone(zoneId, request);
        return ResponseEntity.ok(zoneId);
    }

    @DeleteMapping("{zoneId}")
    public ResponseEntity<Void> removeZone(@PathVariable("zoneId") Long zoneId) {

        zoneService.removeZone(zoneId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
