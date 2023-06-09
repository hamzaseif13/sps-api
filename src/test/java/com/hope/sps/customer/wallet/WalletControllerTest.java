package com.hope.sps.customer.wallet;

import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletControllerTest {

    @Mock
    private WalletService walletService;

    @InjectMocks
    private WalletController walletController;


    @Test
    void getWallet_ReturnsWalletDTO() {
        // Prepare
        final var loggedInUser = new UserInformation("John", "Doe", "john@example.com", "password", Role.CUSTOMER);
        final var walletDTO = new WalletDTO(1L, new BigDecimal("100.0"));
        when(walletService.getWallet(loggedInUser.getEmail())).thenReturn(walletDTO);

        // Execute
        final ResponseEntity<WalletDTO> response = walletController.getWallet(loggedInUser);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(walletDTO);
    }

    @Test
    void chargeWallet_WithValidRequest_ReturnsWalletId() {
        // Prepare
        final var loggedInUser = new UserInformation("John", "Doe", "john@example.com", "password", Role.CUSTOMER);
        final var request = new ChargeRequest(5);
        final Long walletId = 1L;
        when(walletService.charge(loggedInUser.getEmail(), request)).thenReturn(walletId);

        // Execute
        final ResponseEntity<Long> response = walletController.chargeWallet(loggedInUser, request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(walletId);
    }
}
