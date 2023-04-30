package com.hope.sps.auth;

import com.hope.sps.UserDetails.Role;
import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(final LoginRequest request, final String flag) {
        var authentication = authenticateLoginRequest(request);

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();

        verifyAuthTrail(userDetails, flag);

        final String token = jwtUtils.generateToken(userDetails);

        return new AuthenticationResponse(userDetails.getEmail(), token, userDetails.getRole());
    }

    private void verifyAuthTrail(final UserDetailsImpl userDetails, final String flag) {
        if (flag.equals("ADMIN"))
            throwExceptionIfNonAdmin(userDetails);
        else if (flag.equals("NON_ADMIN"))
            throwExceptionIfAdmin(userDetails);
    }


    private void throwExceptionIfNonAdmin(final UserDetailsImpl userDetails) {
        boolean isAdmin = isAdminTryingToLogin(userDetails);

        if (!isAdmin)
            throw new BadCredentialsException("Officers and customers are not allowed to login here");
    }

    private void throwExceptionIfAdmin(final UserDetailsImpl userDetails) {
        boolean isAdmin = isAdminTryingToLogin(userDetails);

        if (isAdmin)
            throw new BadCredentialsException("Admins are not allowed to login here");
    }

    private boolean isAdminTryingToLogin(final UserDetailsImpl userDetails) {
        return userDetails.getRole() == Role.ADMIN;
    }

    private Authentication authenticateLoginRequest(final LoginRequest request) {
        System.err.println(request);
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
    }
}
