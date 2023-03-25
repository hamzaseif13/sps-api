package com.hope.sps.auth;

public record LoginRequest(
        String email,
        String password
) {

}