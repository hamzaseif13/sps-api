package com.hope.sps.customer.payment.wallet;

import com.hope.sps.user_information.UserInformation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/wallet")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('CUSTOMER')")
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<WalletDTO> getWallet(
            @AuthenticationPrincipal
            UserInformation loggedInUser
    ) {

        final WalletDTO walletDTO = walletService.getWallet(loggedInUser.getEmail());
        return ResponseEntity.ok(walletDTO);
    }

    @PatchMapping("charge")
    public ResponseEntity<Long> chargeWallet(
            @AuthenticationPrincipal
            UserInformation loggedInUser,
            @RequestBody @Valid
            ChargeRequest request
    ) {

        final Long walletId = walletService.charge(loggedInUser.getEmail(), request);
        return ResponseEntity.ok(walletId);
    }
}
