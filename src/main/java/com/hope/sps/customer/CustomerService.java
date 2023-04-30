package com.hope.sps.customer;

import com.hope.sps.UserDetails.Role;
import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.auth.AuthenticationResponse;
import com.hope.sps.customer.payment.Wallet;
import com.hope.sps.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final ModelMapper mapper;

    private final CustomerRepository customerRepository;

    private final JwtUtils jwtUtils;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse registerCustomer(CustomerRegisterRequest request) {
        final UserDetailsImpl customerDetails = mapper.map(request, UserDetailsImpl.class);
        final Wallet customerWallet = new Wallet(10.0);
        final Customer customer = new Customer(customerDetails, customerWallet, request.getPhoneNumber());
        customerDetails.setPassword(passwordEncoder.encode(request.getPassword()));

        customerRepository.save(customer);

        final String jwtToken = jwtUtils.generateToken(customerDetails);

        return new AuthenticationResponse(request.getEmail(), jwtToken, Role.CUSTOMER);
    }
}
