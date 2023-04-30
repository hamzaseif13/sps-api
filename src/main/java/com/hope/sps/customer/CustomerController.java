package com.hope.sps.customer;


import com.hope.sps.auth.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll() {

        return null;
    }

    @PostMapping
    public ResponseEntity<AuthenticationResponse> registerCustomer(
            @Valid
            @RequestBody
            CustomerRegisterRequest request) {

        final AuthenticationResponse authResp = customerService.registerCustomer(request);

        return ResponseEntity.ok(authResp);
    }

    @PutMapping
    public ResponseEntity<Long> updateCustomer() {

        return null;
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCustomer() {

        return null;
    }
}
