package com.hope.sps.auth;

import com.hope.sps.UserDetails.Role;

public record AuthenticationResponse(
        String email,
        String jwtToken,
        Role role
) {

//id token role for login and register
// admin id just token and id
}