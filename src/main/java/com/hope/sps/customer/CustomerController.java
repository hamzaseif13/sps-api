package com.hope.sps.customer;


import com.hope.sps.auth.AuthenticationResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // Only admin can get all customer's information
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAll() {
        var customerDTOs = customerService.getAllCustomers();

        if (customerDTOs.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return ResponseEntity.ok(customerDTOs);
    }

    // Only admin can get customer's information
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{customerId}")
    public ResponseEntity<CustomerDTO> getOne(
            @PathVariable("customerId")
            @Validated @Positive
            Long customerId
    ) {
        var customerDTOs = customerService.findById(customerId);
        return ResponseEntity.ok(customerDTOs);
    }


    @PostMapping
    public ResponseEntity<AuthenticationResponse> registerCustomer(
            @RequestBody @Valid
            CustomerRegisterRequest request) {

        final AuthenticationResponse authResp = customerService.registerCustomer(request);
        return ResponseEntity.ok(authResp);
    }
}
