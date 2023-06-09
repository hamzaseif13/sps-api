package com.hope.sps.auth;

import com.hope.sps.jwt.JwtUtils;
import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    private final String ADMIN_FLAG = "ADMIN";

    private final String NON_ADMIN_FLAG = "NON_ADMIN";

    private final String MOCKED_JWT = "mocked_token";

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("authenticate valid admin request and in ADMIN flag")
    void authenticate_withValidRequestAndAdminFlag_returnsAuthenticationResponse() {
        // Prepare
        final var request = new LoginRequest("admin@example.com", "password");
        final var userInformation = new UserInformation("John", "Doe", "admin@example.com", "password", Role.ADMIN);
        final Authentication authentication = new UsernamePasswordAuthenticationToken(userInformation, null);

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtUtils.generateToken(userInformation)).thenReturn(MOCKED_JWT);

        // Execute
        final AuthenticationResponse response = authenticationService.authenticate(request, ADMIN_FLAG);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.firstName()).isEqualTo("John");
        assertThat(response.lastName()).isEqualTo("Doe");
        assertThat(response.email()).isEqualTo("admin@example.com");
        assertThat(response.jwtToken()).isEqualTo(MOCKED_JWT);
        assertThat(response.role()).isEqualTo(Role.ADMIN);

        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(jwtUtils).generateToken(userInformation);
    }

    @Test
    @DisplayName("authenticate valid customer request and in ADMIN flag")
    void authenticate_withValidRequestAndNonAdminFlag_throwsBadCredentialsException() {
        // Prepare
        final var request = new LoginRequest("user@example.com", "password");
        final var userInformation = new UserInformation("Jane", "Doe", "user@example.com", "password", Role.CUSTOMER);
        final Authentication authentication = new UsernamePasswordAuthenticationToken(userInformation, null);

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        // Execute & Verify
        assertThatExceptionOfType(BadCredentialsException.class)
                .isThrownBy(() -> authenticationService.authenticate(request, ADMIN_FLAG));

        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(jwtUtils, never()).generateToken(userInformation);
    }

    @Test
    @DisplayName("authenticate valid customer request and in NON_ADMIN flag")
    void authenticate_withValidRequestAndNonAdminFlag_returnsAuthenticationResponse() {
        // Prepare
        final var request = new LoginRequest("user@example.com", "password");
        final var userInformation = new UserInformation("Jane", "Doe", "user@example.com", "password", Role.CUSTOMER);
        final Authentication authentication = new UsernamePasswordAuthenticationToken(userInformation, null);

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(jwtUtils.generateToken(userInformation)).thenReturn(MOCKED_JWT);

        // Execute
        final AuthenticationResponse response = authenticationService.authenticate(request, NON_ADMIN_FLAG);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.firstName()).isEqualTo("Jane");
        assertThat(response.lastName()).isEqualTo("Doe");
        assertThat(response.email()).isEqualTo("user@example.com");
        assertThat(response.jwtToken()).isEqualTo(MOCKED_JWT);
        assertThat(response.role()).isEqualTo(Role.CUSTOMER);

        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(jwtUtils).generateToken(userInformation);
    }

    @Test
    @DisplayName("authenticate valid admin request and in NON_ADMIN flag")
    void authenticate_withAdminRequestAndNonAdminFlag_throwsBadCredentialsException() {
        // Arrange
        final var request = new LoginRequest("admin@example.com", "password");
        final var userInformation = new UserInformation("John", "Doe", "admin@example.com", "password", Role.ADMIN);
        final Authentication authentication = new UsernamePasswordAuthenticationToken(userInformation, null);

        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        // Execute & Verify
        assertThatExceptionOfType(BadCredentialsException.class)
                .isThrownBy(() -> authenticationService.authenticate(request, NON_ADMIN_FLAG));

        verify(authenticationManager).authenticate(any(Authentication.class));
        verify(jwtUtils, never()).generateToken(userInformation);
    }
}
