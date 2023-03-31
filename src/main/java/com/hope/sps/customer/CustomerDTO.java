package com.hope.sps.customer;

public record CustomerDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        Long phone
) {
}
