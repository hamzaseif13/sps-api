package com.hope.sps.auth;

import com.hope.sps.UserInformation.Role;
import com.hope.sps.UserInformation.UserInformation;
import com.hope.sps.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(final LoginRequest request, final String flag) {
        var authentication = authenticateLoginRequest(request);

        var userInformation = (UserInformation) authentication.getPrincipal();

        verifyAuthTrail(userInformation, flag);

        final String token = jwtUtils.generateToken(userInformation);

        return new AuthenticationResponse(
                userInformation.getEmail(),
                token,
                userInformation.getRole(),
                userInformation.getFirstName(),
                userInformation.getLastName()
        );
    }

    private void verifyAuthTrail(final UserInformation userInformation, final String flag) {
        if (flag.equals("ADMIN"))
            throwExceptionIfNonAdmin(userInformation);
        else if (flag.equals("NON_ADMIN"))
            throwExceptionIfAdmin(userInformation);
    }


    private void throwExceptionIfNonAdmin(final UserInformation userInformation) {
        boolean isAdmin = isAdminTryingToLogin(userInformation);

        if (!isAdmin)
            throw new BadCredentialsException("Officers and customers are not allowed to login here");
    }

    private void throwExceptionIfAdmin(final UserInformation userInformation) {
        boolean isAdmin = isAdminTryingToLogin(userInformation);

        if (isAdmin)
            throw new BadCredentialsException("Admins are not allowed to login here");
    }

    private boolean isAdminTryingToLogin(final UserInformation userInformation) {
        return userInformation.getRole() == Role.ADMIN;
    }

    private Authentication authenticateLoginRequest(final LoginRequest request) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
    }
}
