package com.hope.sps.auth;

import com.hope.sps.jwt.JwtUtils;
import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
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

    // flag parameter can be either ADMIN or NON_ADMIN,
    // ADMIN: indicating that this method is being called from 'authenticateAdmin method'
    // so non-admins login requests are ignored.
    // NON_ADMIN: indicating that this method is being called from 'authenticateOfficerAndCustomer method'
    // so admin login requests are ignored.
    public AuthenticationResponse authenticate(final LoginRequest request, final String flag) {
        // get an authentication object from request's email and request's password
        // trying to authenticate if fail the exception thrown and caught
        var authentication = authenticateLoginRequest(request);

        // authenticate success, get the userInformation object
        var userInformation = (UserInformation) authentication.getPrincipal();

        // if the request comes from an end point where only ADMIN needed to access (flag string),
        // then if NON_ADMIN trying to access an exception will be thrown and caught
        // and vice versa if end point only NON_ADMIN can access
        verifyAuthTrail(userInformation, flag);

        // authentication and authorization success get JWT token
        final String token = jwtUtils.generateToken(userInformation);

        // assemble AuthenticationResponse object for the client, and return it
        return new AuthenticationResponse(
                userInformation.getFirstName(),
                userInformation.getLastName(),
                userInformation.getEmail(),
                token,
                userInformation.getRole()
        );
    }

    /************ HELPER_METHODS **************/
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
