package com.hope.sps.auth;

import com.hope.sps.user_information.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    void authenticateAdmin_WithValidRequest_ReturnsAuthenticationResponse() {
        // Prepare
        final var request = new LoginRequest("admin@example.com", "password");
        final var response = new AuthenticationResponse("John", "Doe", "admin@example.com", "mocked_token", Role.ADMIN);

        when(authenticationService.authenticate(request, "ADMIN")).thenReturn(response);

        // Execute
        final ResponseEntity<AuthenticationResponse> entity = authenticationController.authenticateAdmin(request);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(response);

        verify(authenticationService).authenticate(request, "ADMIN");
    }

    @Test
    void authenticateOfficerAndCustomer_WithValidRequest_ReturnsAuthenticationResponse() {
        // Arrange
        final var request = new LoginRequest("user@example.com", "password");
        final var response = new AuthenticationResponse("Jane", "Doe", "user@example.com", "mocked_token", Role.CUSTOMER);

        when(authenticationService.authenticate(request, "NON_ADMIN")).thenReturn(response);

        // Execute
        final ResponseEntity<AuthenticationResponse> entity = authenticationController.authenticateOfficerAndCustomer(request);

        // Assert
        assertThat(entity).isNotNull();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isEqualTo(response);

        verify(authenticationService).authenticate(request, "NON_ADMIN");
    }

}
