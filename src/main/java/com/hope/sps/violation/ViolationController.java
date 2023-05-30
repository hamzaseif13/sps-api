package com.hope.sps.violation;

import com.hope.sps.user_information.UserInformation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@PreAuthorize("hasAuthority('OFFICER')")
@RequestMapping("api/v1/violation")
@RequiredArgsConstructor
public class ViolationController {

    private final ViolationService violationService;

    @PostMapping
    public ResponseEntity<Void> reportViolation(
            @RequestBody
            @Valid
            ReportViolationRequest request,
            @AuthenticationPrincipal
            UserInformation loggedInOfficer
    ) {

        violationService.reportViolation(request, loggedInOfficer.getEmail());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
