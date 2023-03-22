package com.hope.sps.admin;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.dto.AssignScheduleRequest;
import com.hope.sps.dto.NewZoneRequest;
import com.hope.sps.dto.OfficerRegisterRequest;
import com.hope.sps.dto.UpdateZoneRequest;
import com.hope.sps.officer.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/officer")
    public ResponseEntity<?> registerOfficer(@RequestBody OfficerRegisterRequest request) {
        adminService.registerOfficer(request);
        return ResponseEntity.created(URI.create("/officer")).body("officer created");
    }

    @PutMapping("/officer/{id}/schedule")
    public ResponseEntity<?> assignSchedule(@RequestBody AssignScheduleRequest request,
                                            @PathVariable Long id) {
        adminService.assignSchedule(request, id);
        return ResponseEntity.ok("Officer Registered");
    }

    @PostMapping("/zone")
    public ResponseEntity<?> createZone(@RequestBody NewZoneRequest request) {
        adminService.addParkingZone(request);
        return ResponseEntity.created(URI.create("/zone")).body("Zone created");
    }

    @PostMapping("/zone/{id}")
    public ResponseEntity<String> updateZone(@RequestBody UpdateZoneRequest request, @PathVariable Long id) {
        adminService.updateZone(request,id);
        return ResponseEntity.ok("Updated");
    }
}
