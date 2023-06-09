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

    // Only users with ADMIN role can proceed with this end point,
    // either CUSTOMER nor OFFICER Roles are allowed to be authenticated here.
    // This endpoint must be used to authenticate only ADMIN.
    // So officers or customers are not allowed, for example, to access admin dashboard
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticateAdmin(
            @RequestBody @Valid
            LoginRequest request
    ) {

        var authResp = authenticationService.authenticate(request, "ADMIN");
        return ResponseEntity.ok(authResp);
    }


    // Only users with CUSTOMER or OFFICER role can proceed with this end point,
    // admins are NOT allowed to be authenticated here.
    // This endpoint must be used to authenticate only CUSTOMER or OFFICER.
    // So admins are not allowed, for example, to access the mobile application
    @PostMapping("/login_mobile")
    public ResponseEntity<AuthenticationResponse> authenticateOfficerAndCustomer(
            @RequestBody @Valid
            LoginRequest request
    ) {

        var authResp = authenticationService.authenticate(request, "NON_ADMIN");
        return ResponseEntity.ok(authResp);
    }
}
