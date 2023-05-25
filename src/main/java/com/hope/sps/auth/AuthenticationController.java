package com.hope.sps.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")//admin login, officer and customer not allowed
    public ResponseEntity<AuthenticationResponse> authenticateAdmin(
            @RequestBody @Valid
            LoginRequest request
    ) {

        var authResp = authenticationService.authenticate(request, "ADMIN");
        return ResponseEntity.ok(authResp);
    }


    @PostMapping("/login_mobile")//officer and customer login, admin no allowed
    public ResponseEntity<AuthenticationResponse> authenticateOfficerAndCustomer(
            @RequestBody @Valid
            LoginRequest request
    ) {

        var authResp = authenticationService.authenticate(request, "NON_ADMIN");
        return ResponseEntity.ok(authResp);
    }
}
