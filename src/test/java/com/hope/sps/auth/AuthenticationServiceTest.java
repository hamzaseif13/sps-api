package com.hope.sps.auth;

import com.hope.sps.UserInformation.Role;
import com.hope.sps.UserInformation.UserInformation;
import com.hope.sps.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private LoginRequest testLoginRequest;

    private UserInformation testAdminUserInformation;

    private UserInformation testNonAdminUserInformation;

    @BeforeEach
    public void setUp() {
        testLoginRequest = new LoginRequest("JohnDoe@gmail.com", "John1234");

        testAdminUserInformation = new UserInformation(
                "John@gmail.com",
                "ENCODED_PASSWORD",
                "John",
                "Doe",
                Role.ADMIN
        );

        testNonAdminUserInformation = new UserInformation(
                "Lily@gmail.com",
                "ENCODED_PASSWORD",
                "Lily",
                "Doe",
                Role.CUSTOMER
        );
    }

    @Test
    @DisplayName("test authenticate(final LoginRequest request, ADMIN) allowed")
    void testAuthenticate_ADMIN_adminTryingToLoginIn_shouldAllowHimAndReturnAuthResp() {

        final var mockAuthentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(
                        any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        Mockito.when(mockAuthentication.getPrincipal())
                .thenReturn(testAdminUserInformation);

        Mockito.when(jwtUtils.generateToken(testAdminUserInformation))
                .thenReturn("TEST_JWT_TOKEN");

        final AuthenticationResponse authResp = authenticationService
                .authenticate(testLoginRequest, "ADMIN");

        assertThat(authResp.email()).isEqualTo(testAdminUserInformation.getEmail());
        assertThat(authResp.jwtToken()).isEqualTo("TEST_JWT_TOKEN");
        assertThat(authResp.role()).isEqualTo(testAdminUserInformation.getRole());
    }

    @Test
    @DisplayName("test authenticate(final LoginRequest request, ADMIN) not allowed")
    void testAuthenticate_ADMIN_nonAdminTryingToLoginIn_shouldNotAllowHimAndThrowBadCredentialsException() {

        final var mockAuthentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(
                        any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        Mockito.when(mockAuthentication.getPrincipal())
                .thenReturn(testNonAdminUserInformation);

        assertThatExceptionOfType(BadCredentialsException.class)
                .isThrownBy(() -> authenticationService.authenticate(testLoginRequest, "ADMIN"));
    }

    @Test
    @DisplayName("test authenticate(final LoginRequest request, NON_ADMIN) allowed")
    void testAuthenticate_NON_ADMIN_nonAdminTryingToLoginIn_shouldAllowHimAndReturnAuthResp() {

        final var mockAuthentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(
                        any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        Mockito.when(mockAuthentication.getPrincipal())
                .thenReturn(testNonAdminUserInformation);

        Mockito.when(jwtUtils.generateToken(testNonAdminUserInformation))
                .thenReturn("TEST_JWT_TOKEN");

        final AuthenticationResponse authResp = authenticationService
                .authenticate(testLoginRequest, "NON_ADMIN");

        assertThat(authResp.email()).isEqualTo(testNonAdminUserInformation.getEmail());
        assertThat(authResp.jwtToken()).isEqualTo("TEST_JWT_TOKEN");
        assertThat(authResp.role()).isEqualTo(testNonAdminUserInformation.getRole());
    }

    @Test
    @DisplayName("test authenticate(final LoginRequest request, NON_ADMIN) not allowed")
    void testAuthenticate_NON_ADMIN_AdminTryingToLoginIn_shouldNotAllowHimAndThrowBadCredentialsException() {

        final var mockAuthentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(
                        any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        Mockito.when(mockAuthentication.getPrincipal())
                .thenReturn(testAdminUserInformation);

        assertThatExceptionOfType(BadCredentialsException.class)
                .isThrownBy(() -> authenticationService.authenticate(testLoginRequest, "NON_ADMIN"));
    }
}