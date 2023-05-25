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
        if (customerRepository.existsByUserInformationEmail(request.getEmail()))
            throw new DuplicateResourceException("email already exists");

        if (!request.getPassword().matches(Validator.passwordValidationRegex))
            throw new InvalidResourceProvidedException("invalid password");

        final var customerInformation = mapper.map(request, UserInformation.class);
        customerInformation.setRole(Role.CUSTOMER);
        customerInformation.setPassword(passwordEncoder.encode(request.getPassword()));

        final var customerWallet = new Wallet(new BigDecimal("10.0"));

        final var customer = new Customer(customerInformation, customerWallet, request.getPhoneNumber());

        customerRepository.save(customer);

        final String jwtToken = jwtUtils.generateToken(customerInformation);
        return new AuthenticationResponse(
                request.getEmail(),
                jwtToken,
                Role.CUSTOMER,
                customerInformation.getFirstName(),
                customerInformation.getLastName()
        );
    }

    public CustomerDTO findById(Long customerId) {
        return mapper.map(customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("customer doesn't exists")
                ), CustomerDTO.class
        );
    }
}
