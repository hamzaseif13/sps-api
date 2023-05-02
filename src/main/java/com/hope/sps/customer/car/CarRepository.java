package com.hope.sps.customer.car;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

    boolean existsByPlateNumber(String plateNumber);
}
