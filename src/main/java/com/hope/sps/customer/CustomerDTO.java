package com.hope.sps.customer;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "firstName", "lastName", "email", "phoneNumber"})
public class CustomerDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;
}
