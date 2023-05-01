package com.hope.sps.customer.payment.wallet;

import com.hope.sps.UserInformation.UserInformation;
import com.hope.sps.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    private final CustomerRepository customerRepository;

    @Transactional
    public Long charge(final UserInformation loggedInUser, final ChargeRequest request) {
        final var customer = customerRepository.findByUserInformationEmail(loggedInUser.getEmail())
                .orElseThrow();

        walletRepository.updateBalance(customer.getWallet().getId(), request.getAmountToCharge());

        return customer.getWallet().getId();
    }
}
