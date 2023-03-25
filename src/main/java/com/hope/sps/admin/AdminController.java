package com.hope.sps.admin;

import com.hope.sps.dto.AssignScheduleRequest;
import com.hope.sps.dto.NewZoneRequest;
import com.hope.sps.dto.RegisterRequest;
import com.hope.sps.dto.UpdateZoneRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    //register admin Done
    @PostMapping
    public ResponseEntity<Long> register(
            @RequestBody
            @Valid
            RegisterRequest request) {

        Long adminId = adminService.registerAdmin(request);

        return ResponseEntity.ok(adminId);
    }

    @PutMapping("/officer/{id}/schedule")
    public ResponseEntity<String> assignSchedule(
            @Valid
            @RequestBody AssignScheduleRequest request,
            @PathVariable Long id) {

        adminService.assignSchedule(request, id);

        return ResponseEntity.ok("schedule Created");
    }

    @PostMapping("/zone")
    public ResponseEntity<String> createZone(
            @Valid
            @RequestBody NewZoneRequest request) {
        adminService.addParkingZone(request);

        return ResponseEntity.created(URI.create("/zone")).body("Zone created");
    }

    @PostMapping("/zone/{id}")
    public ResponseEntity<String> updateZone(
            @Valid
            @RequestBody UpdateZoneRequest request,
            @PathVariable Long id) {
        adminService.updateZone(request, id);

        return ResponseEntity.ok("Updated");
    }
}
