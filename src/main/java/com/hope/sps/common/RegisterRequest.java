package com.hope.sps.common;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Length(min = 3, max = 20, message = "first Name must be between 3 and 20 chars")
    private String firstName;

    @Length(min = 3, max = 20, message = "last name must be between 3 and 20 chars")
    private String lastName;

    @Email(message = "invalid email schema")
    private String email;

    @Length(min = 8, max = 64, message = "password must be between 8 and 64 chars")
    private String password;
}
