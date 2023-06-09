package com.hope.sps.customer;

import com.hope.sps.auth.AuthenticationResponse;
import com.hope.sps.user_information.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @Test
    void getAll_WithNoCustomers_ReturnsNoContent() {
        // Prepare
        when(customerService.getAllCustomers()).thenReturn(new ArrayList<>());

        // Execute
        final ResponseEntity<List<CustomerDTO>> response = customerController.getAll();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void getAll_WithCustomers_ReturnsCustomerDTOs() {
        // Prepare
        final var customerDTOs = new ArrayList<CustomerDTO>();
        customerDTOs.add(new CustomerDTO(1L, "John", "Doe", "john@example.com", "1234567890"));
        customerDTOs.add(new CustomerDTO(2L, "Jane", "Smith", "jane@example.com", "9876543210"));
        when(customerService.getAllCustomers()).thenReturn(customerDTOs);

        // Execute
        final ResponseEntity<List<CustomerDTO>> response = customerController.getAll();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(2);
    }

    @Test
    void getOne_WithValidCustomerId_ReturnsCustomerDTO() {
        // Prepare
        final Long customerId = 1L;
        final var customerDTO = new CustomerDTO(customerId, "John", "Doe", "john@example.com", "1234567890");
        when(customerService.findById(customerId)).thenReturn(customerDTO);

        // Execute
        final ResponseEntity<CustomerDTO> response = customerController.getOne(customerId);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(customerDTO);
    }


    @Test
    void registerCustomer_WithValidRequest_ReturnsAuthenticationResponse() {
        // Prepare
        final var request = new CustomerRegisterRequest("1234567890", "John", "Doe", "john@example.com", "password");
        when(customerService.registerCustomer(request)).thenReturn(new AuthenticationResponse("John", "Doe", "john@example.com", "token", Role.CUSTOMER));

        // Execute
        final ResponseEntity<AuthenticationResponse> response = customerController.registerCustomer(request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}
