package com.hope.sps.admin;

import com.hope.sps.UserDetails.Role;
import com.hope.sps.UserDetails.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AdminDTOMapperTest {

    private final AdminDTOMapper mapper = new AdminDTOMapper();

    @Test
    @DisplayName("test AdminDTOMapper's apply(Admin admin)")
    void testApply_shouldMapFromAdminToAdminDTO() {
        final var testUserDetailsImpl = new UserDetailsImpl(
                "John@gmail.com",
                "ENCODED_PASSWORD",
                "John",
                "Doe",
                Role.ADMIN
        );

        final var testAdmin = new Admin(1L, testUserDetailsImpl);

        final AdminDTO testAdminDTO = mapper.apply(testAdmin);

        assertThat(testAdminDTO.id()).isEqualTo(testAdmin.getId());
        assertThat(testAdminDTO.email()).isEqualTo(testAdmin.getUserDetails().getEmail());
        assertThat(testAdminDTO.firstName()).isEqualTo(testAdmin.getUserDetails().getFirstName());
        assertThat(testAdminDTO.lastName()).isEqualTo(testAdmin.getUserDetails().getLastName());
    }
}