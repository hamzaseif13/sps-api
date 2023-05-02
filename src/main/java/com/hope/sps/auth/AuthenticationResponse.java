package com.hope.sps.auth;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hope.sps.UserInformation.Role;

@JsonPropertyOrder({"firstName", "lastName", "email", "jwtToken", "role"})
public record AuthenticationResponse(
        String email,
        String jwtToken,
        Role role,
        String firstName,
        String lastName
) {
}