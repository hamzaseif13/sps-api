package com.hope.sps.officer;

import com.hope.sps.auth.AuthenticationService;
import com.hope.sps.auth.LoginRequest;
import com.hope.sps.dto.OfficerRegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/officer")
@RequiredArgsConstructor
public class OfficerController {

    private final OfficerService officerService;

    private final AuthenticationService authenticationService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")//todo hasRole or authority
    public ResponseEntity<List<OfficerDTO>> getAll() {

        List<OfficerDTO> officerDTOList = officerService.getAll();

        return ResponseEntity.ok(officerDTOList);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> registerOfficer(
            @RequestBody
            @Valid
            OfficerRegisterRequest request) {

        Long officerId = officerService.registerOfficer(request);

        return new ResponseEntity<>(officerId, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<OfficerDTO> getDetailsById(){

        return null;
    }
    @PutMapping("{Id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateOfficer(
            @RequestBody
            @Valid
            OfficerUpdateRequest request,
            @PathVariable("Id") Long officerId) {

        officerService.updateOfficer(request, officerId);

        return ResponseEntity.ok("schedule Created");
    }
}
