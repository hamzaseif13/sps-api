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

}
