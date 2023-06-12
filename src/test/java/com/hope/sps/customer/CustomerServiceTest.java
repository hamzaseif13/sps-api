package com.hope.sps.customer;

import com.hope.sps.auth.AuthenticationResponse;
import com.hope.sps.exception.DuplicateResourceException;
import com.hope.sps.exception.InvalidResourceProvidedException;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.jwt.JwtUtils;
import com.hope.sps.user_information.UserInformation;
import com.hope.sps.util.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Validator validator;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void getAllCustomers_ReturnsListOfCustomerDTOs() {
        // Prepare
        final var customers = new ArrayList<Customer>();
        customers.add(new Customer());
        customers.add(new Customer());

        when(customerRepository.findAll()).thenReturn(customers);
        when(modelMapper.map(any(), eq(CustomerDTO.class))).thenReturn(new CustomerDTO());

        // Execute
        final List<CustomerDTO> customerDTOs = customerService.getAllCustomers();

        // Assert
        assertThat(customerDTOs).isNotNull();
        assertThat(customers.size()).isEqualTo(customerDTOs.size());
        verify(customerRepository, times(1)).findAll();
        verify(modelMapper, times(customers.size())).map(any(), eq(CustomerDTO.class));
    }

    @Test
    void registerCustomer_WithValidData_ReturnsAuthenticationResponse() {
        // Prepare
        final var request = new CustomerRegisterRequest("John", "Doe", "test@example.com", "password", "1234567890");
        final var userInformation = new UserInformation();

        when(customerRepository.existsByUserInformationEmail(request.getEmail())).thenReturn(false);
        when(validator.validateUserPassword(request.getPassword())).thenReturn(true);
        when(modelMapper.map(request, UserInformation.class)).thenReturn(userInformation);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");
        when(jwtUtils.generateToken(userInformation)).thenReturn("jwtToken");

        // Execute
        final AuthenticationResponse response = customerService.registerCustomer(request);

        // Assert
        assertThat(response).isNotNull();
        verify(customerRepository, times(1)).existsByUserInformationEmail(request.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(validator, times(1)).validateUserPassword(request.getPassword());
        verify(modelMapper, times(1)).map(request, UserInformation.class);
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(jwtUtils, times(1)).generateToken(userInformation);
    }

    @Test
    void registerCustomer_WithExistingEmail_ThrowsDuplicateResourceException() {
        // Prepare
        final var request = new CustomerRegisterRequest("John", "Doe", "test@example.com", "password", "1234567890");

        when(customerRepository.existsByUserInformationEmail(request.getEmail())).thenReturn(true);

        // Execute and Assert
        assertThatExceptionOfType(DuplicateResourceException.class)
                .isThrownBy(() -> customerService.registerCustomer(request));

        verify(customerRepository, times(1)).existsByUserInformationEmail(request.getEmail());
        verifyNoInteractions(validator);
        verifyNoInteractions(modelMapper);
        verifyNoInteractions(passwordEncoder);
        verifyNoMoreInteractions(customerRepository);
        verifyNoInteractions(jwtUtils);
    }

    @Test
    void registerCustomer_WithInvalidPassword_ThrowsInvalidResourceProvidedException() {
        // Prepare
        final var request = new CustomerRegisterRequest("John", "Doe", "test@example.com", "password", "1234567890");

        when(customerRepository.existsByUserInformationEmail(request.getEmail())).thenReturn(false);
        when(validator.validateUserPassword(request.getPassword())).thenReturn(false);

        // Execute and Assert
        assertThatExceptionOfType(InvalidResourceProvidedException.class)
                .isThrownBy(() -> customerService.registerCustomer(request));


        verify(customerRepository, times(1)).existsByUserInformationEmail(request.getEmail());
        verify(validator, times(1)).validateUserPassword(request.getPassword());
        verifyNoInteractions(modelMapper);
        verifyNoInteractions(passwordEncoder);
        verifyNoMoreInteractions(customerRepository);
        verifyNoInteractions(jwtUtils);
    }

    @Test
    void findById_WithValidCustomerId_ReturnsCustomerDTO() {
        // Prepare
        final Long customerId = 1L;
        final var customer = new Customer();
        customer.setId(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(modelMapper.map(customer, CustomerDTO.class)).thenReturn(new CustomerDTO());

        // Execute
        final CustomerDTO customerDTO = customerService.findById(customerId);

        // Assert
        assertThat(customerDTO).isNotNull();
        verify(customerRepository, times(1)).findById(customerId);
        verify(modelMapper, times(1)).map(customer, CustomerDTO.class);
    }

    @Test
    void findById_WithInvalidCustomerId_ThrowsResourceNotFoundException() {
        // Prepare
        final Long customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Execute and Assert
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> customerService.findById(customerId));

        verify(customerRepository, times(1)).findById(customerId);
        verifyNoInteractions(modelMapper);
    }

}
