package com.hope.sps.customer;

import com.hope.sps.common.RegisterRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegisterRequest extends RegisterRequest {

    @Length(min = 10, max = 20, message = "phoneNumber must be between 10 and 20 chars")
    @NotEmpty(message = "phoneNumber is required")
    private String phoneNumber;

    // for testing
    public CustomerRegisterRequest(
            String firstName,
            String lastName,
            String email,
            String password,
            String phoneNumber
    ) {
        super(firstName, lastName, email, password);
        this.phoneNumber = phoneNumber;
    }
}
