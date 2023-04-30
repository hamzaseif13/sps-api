package com.hope.sps.auth;

import com.hope.sps.UserDetails.Role;

public record AuthenticationResponse(
        String email,
        String jwtToken,
        Role role
        //first name and lastName
) {
}