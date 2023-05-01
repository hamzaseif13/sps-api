package com.hope.sps.customer.payment.wallet;

import com.hope.sps.UserInformation.UserInformation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PatchMapping("charge")
    public ResponseEntity<Long> chargeWallet(
            @AuthenticationPrincipal
            UserInformation loggedInUser,
            @RequestBody @Valid
            ChargeRequest request
    ) {

        final Long walletId = walletService.charge(loggedInUser, request);
        return ResponseEntity.ok(walletId);
    }

}
