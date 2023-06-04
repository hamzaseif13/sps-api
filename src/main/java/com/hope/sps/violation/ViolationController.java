package com.hope.sps.violation;

import com.hope.sps.user_information.UserInformation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('OFFICER')")
@RequestMapping("api/v1/violation")
@RequiredArgsConstructor
public class ViolationController {

    private final ViolationService violationService;

    @GetMapping
    public ResponseEntity<List<ViolationDTO>> getAll() {
        return ResponseEntity.ok(violationService.getAllViolations());
    }

    @GetMapping("logged-in")
    public ResponseEntity<List<ViolationDTO>> getViolationLoggedInOfficer(
            @AuthenticationPrincipal
            UserInformation loggedInOfficer
    ) {
        final var violationDTOList = violationService
                .getViolationsByOfficerEmail(loggedInOfficer.getEmail());

        return ResponseEntity.ok(violationDTOList);
    }

    @PostMapping
    public ResponseEntity<Void> createViolation(
            @RequestBody @Valid
            ReportViolationRequest request,
            @AuthenticationPrincipal
            UserInformation loggedInOfficer
    ) {
        violationService.createViolation(request, loggedInOfficer.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
