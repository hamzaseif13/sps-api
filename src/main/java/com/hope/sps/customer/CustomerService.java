package com.hope.sps.customer;

import com.hope.sps.auth.AuthenticationResponse;
import com.hope.sps.customer.payment.wallet.Wallet;
import com.hope.sps.exception.DuplicateResourceException;
import com.hope.sps.exception.InvalidResourceProvidedException;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.jwt.JwtUtils;
import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
import com.hope.sps.util.Validator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final ModelMapper mapper;

    @Transactional(readOnly = true)
    public List<CustomerDTO> getAllCustomers() {
        var customerDTOs = new ArrayList<CustomerDTO>();

        customerRepository.findAll().forEach(customer ->
                customerDTOs.add(
                        mapper.map(customer, CustomerDTO.class)
                ));

        return customerDTOs;
    }

    public AuthenticationResponse registerCustomer(final CustomerRegisterRequest request) {
        // an email existing, similar to the one provided in the request?
        throwExceptionIfExistingEmail(request.getEmail());

        // not valid password according to validation policy?
        Validator.validateUserPassword(request.getPassword());

        // here email and password are valid
        // map the CustomerRegisterRequest object to UserInformation Object and set role to CUSTOMER
        final var customerInformation = mapper.map(request, UserInformation.class);
        customerInformation.setRole(Role.CUSTOMER);

        // hash the customer's password
        customerInformation.setPassword(passwordEncoder.encode(request.getPassword()));

        // initialize customer wallet with 10.0 JOD
        final var customerWallet = new Wallet(new BigDecimal("10.0"));

        // assemble a customer object to be persisted
        final var customer = new Customer(customerInformation, customerWallet, request.getPhoneNumber());

        // save the customer
        customerRepository.save(customer);

        // generate token from customerInformation
        final String jwtToken = jwtUtils.generateToken(customerInformation);

        // assemble AuthenticationResponse object for the client, and return it
        return new AuthenticationResponse(
                customerInformation.getFirstName(),
                customerInformation.getLastName(),
                request.getEmail(),
                jwtToken,
                customerInformation.getRole()
        );
    }

    public CustomerDTO findById(final Long customerId) {
        final Customer customerById = customerRepository.findById(customerId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("customer doesn't exists")
                );

        return mapper.map(customerById, CustomerDTO.class);
    }

    /************** HELPER_METHODS *************/

    private void throwExceptionIfExistingEmail(final String customerEmail) {
        if (customerRepository.existsByUserInformationEmail(customerEmail))
            throw new DuplicateResourceException("email already exists");
    }
}
