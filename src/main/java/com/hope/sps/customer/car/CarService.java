package com.hope.sps.customer.car;

import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    private final CustomerRepository customerRepository;

    private final ModelMapper mapper;

    public List<CarDTO> registerNewCar(final String customerEmail, final CarRegistrationRequest request) {
        throwExceptionIfExistingPlateNumber(request.getPlateNumber());

        final Customer loggedInCustomer = getLoggedInCustomer(customerEmail);

        final var customerCar = mapper.map(request, Car.class);
        loggedInCustomer.addCar(customerCar);

        final var savedCustomer = customerRepository.save(loggedInCustomer);

        return mapCarSetToCarDTOs(savedCustomer.getCars());
    }

    @Transactional(readOnly = true)
    public List<CarDTO> findAllCars(final String customerEmail) {
        final Customer loggedInCustomer = getLoggedInCustomer(customerEmail);

        return mapCarSetToCarDTOs(loggedInCustomer.getCars());
    }

    //*************** HELPER_METHODS ********************//

    private void throwExceptionIfExistingPlateNumber(final String plateNumber) {
        if (carRepository.existsByPlateNumber(plateNumber))
            throw new DuplicateResourceException("already exists car's plate number");
    }

    // Note will never throw exception
    private Customer getLoggedInCustomer(final String customerEmail) {
        return customerRepository.findByUserInformationEmail(customerEmail)
                .orElseThrow();
    }


    private List<CarDTO> mapCarSetToCarDTOs(final Set<Car> customerCars) {
        final var customerCarDTOs = new ArrayList<CarDTO>();

        customerCars.forEach(car ->
                customerCarDTOs.add(mapper.map(car, CarDTO.class))
        );

        return customerCarDTOs;
    }
}
