package com.hope.sps.customer;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.dto.RegisterRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {


    private final RegisterRequestMapper registerRequestMapper;

    private final CustomerRepository customerRepository;

    public Long registerCustomer(CustomerRegisterRequest request) {
        UserDetailsImpl customerDetails = registerRequestMapper.apply(request);
        Wallet customerWallet = new Wallet(0.0);
        Customer customer = new Customer(customerDetails, customerWallet);

        return customerRepository.save(customer).getId();
    }
}
