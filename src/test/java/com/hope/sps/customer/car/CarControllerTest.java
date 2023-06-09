package com.hope.sps.customer.car;

import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    @Test
    void getAll_ReturnsListOfCarDTOs() {
        // Prepare
        final var loggedInCustomerInfo = new UserInformation("John", "Doe", "john@example.com", "password", Role.CUSTOMER);
        final var carDTOs = new ArrayList<CarDTO>();
        carDTOs.add(new CarDTO(1L, "Red", "Toyota", "ABC123"));
        carDTOs.add(new CarDTO(2L, "Blue", "Honda", "XYZ456"));
        when(carService.findAllCars(loggedInCustomerInfo.getEmail())).thenReturn(carDTOs);

        // Execute
        final ResponseEntity<List<CarDTO>> response = carController.getAll(loggedInCustomerInfo);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(carDTOs);
    }

    @Test
    @PreAuthorize("hasAuthority('CUSTOMER')")
    void registerNewCar_WithValidRequest_ReturnsListOfCarDTOs() {
        // Prepare
        final var loggedInCustomerInfo = new UserInformation("John", "Doe", "john@example.com", "password", Role.CUSTOMER);
        final var request = new CarRegistrationRequest("Red", "Toyota", "ABC123");
        final var carDTOs = new ArrayList<CarDTO>();
        carDTOs.add(new CarDTO(1L, "Red", "Toyota", "ABC123"));
        when(carService.registerNewCar(loggedInCustomerInfo.getEmail(), request)).thenReturn(carDTOs);

        // Execute
        final ResponseEntity<List<CarDTO>> response = carController.registerNewCar(loggedInCustomerInfo, request);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(carDTOs);
    }
}
