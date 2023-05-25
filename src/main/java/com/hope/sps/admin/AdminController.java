package com.hope.sps.admin;


import com.hope.sps.common.RegisterRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<AdminDTO>> getAll() {

        final List<AdminDTO> adminDTOS = adminService.getAllAdmins();

        if (adminDTOS.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok(adminDTOS);
    }

    @PostMapping
    public ResponseEntity<Long> register(
            @RequestBody
            @Valid
            RegisterRequest request
    ) {

        final Long adminId = adminService.registerAdmin(request);
        return ResponseEntity.ok(adminId);
    }

    @DeleteMapping("{adminId}")
    public ResponseEntity<Void> deleteById(
            @PathVariable("adminId")
            @Validated @Positive
            Long adminId
    ) {

        adminService.deleteAdminById(adminId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
