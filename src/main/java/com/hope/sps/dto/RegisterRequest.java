package com.hope.sps.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RegisterRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String password;
}
