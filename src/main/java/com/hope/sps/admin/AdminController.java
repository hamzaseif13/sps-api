package com.hope.sps.admin;


import com.hope.sps.dto.RegisterRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
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

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable
            @Validated
            @Positive
            Long id
    ) {

        adminService.deleteAdminById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
