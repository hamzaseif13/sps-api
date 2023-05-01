package com.hope.sps.admin;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "firstName", "lastName", "email"})
public class AdminDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

}
