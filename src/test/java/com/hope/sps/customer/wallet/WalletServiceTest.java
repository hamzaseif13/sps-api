package com.hope.sps.customer.wallet;

import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private WalletService walletService;

    @Test
    void charge_WithValidData_ReturnsWalletId() {
        // Prepare
        final String userEmail = "test@example.com";
        final var request = new ChargeRequest(5);
        final var wallet = new Wallet(1L, BigDecimal.valueOf(10.0));
        final var customer = new Customer();
        customer.setWallet(wallet);

        when(customerRepository.findByUserInformationEmail(userEmail)).thenReturn(Optional.of(customer));

        // Execute
        final Long walletId = walletService.charge(userEmail, request);

        // Assert
        assertThat(walletId).isNotNull();
        assertThat(wallet.getId()).isEqualTo(walletId);
        verify(customerRepository, times(1)).findByUserInformationEmail(userEmail);
    }

    @Test
    void getWallet_WithValidUserEmail_ReturnsWalletDTO() {
        // Prepare
        final String userEmail = "test@example.com";
        final var wallet = new Wallet(1L, BigDecimal.valueOf(10.0));
        final var customer = new Customer();
        customer.setWallet(wallet);

        when(customerRepository.findByUserInformationEmail(userEmail)).thenReturn(Optional.of(customer));
        when(modelMapper.map(wallet, WalletDTO.class)).thenReturn(new WalletDTO());

        // Execute
        final var walletDTO = walletService.getWallet(userEmail);

        // Assert
        assertNotNull(walletDTO);
        verify(customerRepository, times(1)).findByUserInformationEmail(userEmail);
        verify(modelMapper, times(1)).map(wallet, WalletDTO.class);
    }
}
