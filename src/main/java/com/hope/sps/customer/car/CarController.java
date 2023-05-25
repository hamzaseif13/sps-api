package com.hope.sps.customer.car;

import com.hope.sps.user_information.UserInformation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/car")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('CUSTOMER')")
public class CarController {

    private final CarService carService;

    @GetMapping
    public ResponseEntity<List<CarDTO>> getAll(
            @AuthenticationPrincipal
            UserInformation loggedInCustomerInfo
    ) {

        final List<CarDTO> carDTOs = carService.findAllCars(loggedInCustomerInfo);
        return ResponseEntity.ok(carDTOs);
    }

    @PostMapping
    public ResponseEntity<List<CarDTO>> registerNewCar(
            @AuthenticationPrincipal
            UserInformation loggedInCustomerInfo,
            @RequestBody @Valid
            CarRegistrationRequest request
    ) {

        final List<CarDTO> carDTOs = carService.registerNewCar(loggedInCustomerInfo, request);
        return ResponseEntity.ok(carDTOs);
    }
}
