package com.hope.sps.customer;


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
    public ResponseEntity<Long> registerCustomer(
            @Valid
            @RequestBody
            CustomerRegisterRequest request) {

        Long customerId = customerService.registerCustomer(request);

        return ResponseEntity.ok(customerId);
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
