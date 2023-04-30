package com.hope.sps.customer.car;

import com.hope.sps.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Entity
@Table(name = "car")
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Car extends BaseEntity {

    @Column(name = "color", nullable = false, length = 20)
    private String color;

    @Column(name = "brand", nullable = false, length = 50)
    private String brand;

    @Column(name = "plate_number", nullable = false, length = 15)
    @NotNull
    private String plateNumber;

    public Car(Long id) {
        super(id);
    }
}
