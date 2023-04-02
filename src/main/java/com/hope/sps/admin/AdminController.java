package com.hope.sps.admin;


import com.hope.sps.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping
    public ResponseEntity<List<AdminDto>> getAllAdmins(){
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String>deleteAdminById(@PathVariable Long id){
        adminService.deleteAdminById(id);
        return ResponseEntity.ok("delete Successfully");
    }

}
