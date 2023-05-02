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

    @Length(min = 3, max = 20, message = "invalid first Name")
    private String firstName;

    @Length(min = 3, max = 20, message = "invalid last name")
    private String lastName;

    @Email(message = "invalid email")
    private String email;

    @Length(min = 8, max = 64, message = "invalid password")
    private String password;
}
