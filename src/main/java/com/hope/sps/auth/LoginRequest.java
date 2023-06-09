package com.hope.sps.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Email(message = "invalid email schema")
    @NotEmpty(message = "email is mandatory")
    private String email;

    @Size(min = 8, max = 64, message = "password must be between 8 and 64 chars")
    @NotEmpty(message = "password is mandatory")
    private String password;
}