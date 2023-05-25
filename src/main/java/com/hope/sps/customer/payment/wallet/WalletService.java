package com.hope.sps.customer.payment.wallet;

import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final CustomerRepository customerRepository;

    private final ModelMapper mapper;

    @Transactional
    public Long charge(final String userEmail, final ChargeRequest request) {
        final var customer = getLoggedInCustomer(userEmail);

        final BigDecimal walletBalance = customer.getWallet().getBalance();
        final BigDecimal amountToAdd = new BigDecimal(request.getAmountToCharge());

        customer.getWallet().setBalance(walletBalance.add(amountToAdd));
        return customer.getWallet().getId();
    }

    @Transactional(readOnly = true)
    public WalletDTO getWallet(final String userEmail) {
        final var customer = getLoggedInCustomer(userEmail);

        return mapper.map(customer.getWallet(), WalletDTO.class);
    }

    // Retrieving Customer by its email, from the DB.
    // Guarantee to return a customer because only authenticated and authorized customer can reach here
    private Customer getLoggedInCustomer(final String email) {
        return customerRepository.findByUserInformationEmail(email).orElseThrow();
    }
}
