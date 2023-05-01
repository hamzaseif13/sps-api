package com.hope.sps.customer.car;

import com.hope.sps.UserInformation.UserInformation;
import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    private final CustomerRepository customerRepository;

    private final ModelMapper mapper;

    public List<CarDTO> registerNewCar(final UserInformation userInformation, final CarRegistrationRequest request) {
        if (carRepository.existsByPlateNumber(request.getPlateNumber()))
            throw new DuplicateResourceException("already exists car's plate number");

        final Customer customer = customerRepository
                .findByUserInformationEmail(userInformation.getEmail())
                .orElseThrow();

        final var customerCar = mapper.map(request, Car.class);
        customer.addCar(customerCar);

        final var savedCustomer = customerRepository.save(customer);

        return mapCarSetToCarDTOs(savedCustomer);
    }

    @Transactional(readOnly = true)
    public List<CarDTO> findAllCars(UserInformation userInformation) {
        final Customer customer = customerRepository
                .findByUserInformationEmail(userInformation.getEmail())
                .orElseThrow();

        return mapCarSetToCarDTOs(customer);
    }

    private List<CarDTO> mapCarSetToCarDTOs(final Customer customer) {
        final var carDTOs = new ArrayList<CarDTO>();

        customer.getCars().forEach(car ->
                carDTOs.add(mapper.map(car, CarDTO.class))
        );
        return carDTOs;
    }
}
