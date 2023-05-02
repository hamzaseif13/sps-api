package com.hope.sps.customer.car;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "car")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "color", nullable = false, length = 20)
    private String color;

    @Column(name = "brand", nullable = false, length = 50)
    private String brand;

    @Column(name = "plate_number", nullable = false, length = 15, unique = true)
    private String plateNumber;

    public Car(Long id) {
        this.id = id;
    }
}
