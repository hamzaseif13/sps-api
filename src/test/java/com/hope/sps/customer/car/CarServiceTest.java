package com.hope.sps.customer.car;

import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.exception.DuplicateResourceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CarService carService;

    @Test
    void registerNewCar_WithValidData_ReturnsCarDTOList() {
        // Prepare
        final String customerEmail = "test@example.com";
        final var request = new CarRegistrationRequest("Red", "Toyota", "ABC123");
        final var loggedInCustomer = new Customer();
        loggedInCustomer.setCars(new HashSet<>());

        when(customerRepository.findByUserInformationEmail(customerEmail)).thenReturn(Optional.of(loggedInCustomer));
        when(carRepository.existsByPlateNumber(request.getPlateNumber())).thenReturn(false);
        when(modelMapper.map(request, Car.class)).thenReturn(new Car());
        when(customerRepository.save(loggedInCustomer)).thenReturn(loggedInCustomer);
        when(modelMapper.map(any(Car.class), eq(CarDTO.class))).thenReturn(new CarDTO());

        // Execute
        final List<CarDTO> carDTOs = carService.registerNewCar(customerEmail, request);

        // Assert
        assertThat(carDTOs).isNotNull();
        assertThat(carDTOs.isEmpty()).isFalse();
        verify(customerRepository, times(1)).findByUserInformationEmail(customerEmail);
        verify(carRepository, times(1)).existsByPlateNumber(request.getPlateNumber());
        verify(modelMapper, times(1)).map(request, Car.class);
        verify(customerRepository, times(1)).save(loggedInCustomer);
        verify(modelMapper, times(1)).map(any(Car.class), eq(CarDTO.class));
    }

    @Test
    void registerNewCar_WithExistencePlateNumber_ThrowsDuplicateResourceException() {
        // Prepare
        final String customerEmail = "test@example.com";
        final var request = new CarRegistrationRequest("Red", "Toyota", "ABC123");

        when(carRepository.existsByPlateNumber(request.getPlateNumber())).thenReturn(true);

        // Execute
        // Assert
        assertThatExceptionOfType(DuplicateResourceException.class).isThrownBy(() -> carService.registerNewCar(customerEmail, request));

        verifyNoInteractions(customerRepository);
        verify(carRepository, times(1)).existsByPlateNumber(request.getPlateNumber());
        verifyNoInteractions(modelMapper);
    }

    @Test
    void findAllCars_WithValidCustomerEmail_ReturnsCarDTOList() {
        // Prepare
        final String customerEmail = "test@example.com";
        final var loggedInCustomer = new Customer();
        final var car1 = new Car(1L, "RED", "BMW", "123");
        final var car2 = new Car(2L, "BLUE", "BMW", "321");

        final var car1DTO = new CarDTO(1L, "RED", "BMW", "123");
        final var car2DTO = new CarDTO(2L, "BLUE", "BMW", "321");

        loggedInCustomer.setCars(Set.of(car1, car2));

        when(customerRepository.findByUserInformationEmail(customerEmail)).thenReturn(Optional.of(loggedInCustomer));
        when(modelMapper.map(car1, CarDTO.class)).thenReturn(car1DTO);
        when(modelMapper.map(car2, CarDTO.class)).thenReturn(car2DTO);

        // Execute
        final List<CarDTO> carDTOs = carService.findAllCars(customerEmail);

        // Assert
        assertThat(carDTOs).isNotNull();
        assertThat(carDTOs.isEmpty()).isFalse();
        verify(customerRepository, times(1)).findByUserInformationEmail(customerEmail);
    }
}
