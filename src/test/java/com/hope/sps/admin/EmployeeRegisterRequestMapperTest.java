package com.hope.sps.admin;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.dto.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class EmployeeRegisterRequestMapperTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeRegisterRequestMapper mapper;


    @Test
    @DisplayName("test AdminRegisterRequestMapper's apply(RegisterRequest request)")
    void testApply_shouldMapRegisterRequestToUserDetailsImpl() {
        final var testRegisterRequest = new RegisterRequest(
                "John",
                "Doe",
                "John@gmail.com",
                "John1234"
        );

        Mockito.when(passwordEncoder.encode(anyString()))
                .thenReturn("ENCODED_PASSWORD");

        final UserDetailsImpl testUserDetailsImpl = mapper.apply(testRegisterRequest);

        assertThat(testUserDetailsImpl.getFirstName()).isEqualTo(testRegisterRequest.getFirstName());
        assertThat(testUserDetailsImpl.getLastName()).isEqualTo(testRegisterRequest.getLastName());
        assertThat(testUserDetailsImpl.getEmail()).isEqualTo(testRegisterRequest.getEmail());
        assertThat(testUserDetailsImpl.getPassword()).isEqualTo("ENCODED_PASSWORD");
    }
}