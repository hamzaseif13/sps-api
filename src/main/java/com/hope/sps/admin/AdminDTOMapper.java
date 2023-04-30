package com.hope.sps.admin;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AdminDTOMapper implements Function<Admin, AdminDTO> {

    @Override
    public AdminDTO apply(final Admin admin) {

        return new AdminDTO(
                admin.getId(),
                admin.getUserDetails().getFirstName(),
                admin.getUserDetails().getLastName(),
                admin.getUserDetails().getEmail()
        );
    }
}
