package com.hope.sps.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class LoginRequest {

    @Email(message = "invalid email")
    @NotEmpty(message = "password is mandatory")
    private String email;

    @Size(min = 8, max = 64, message = "invalid password size")
    @NotEmpty(message = "password is mandatory")
    private String password;

}