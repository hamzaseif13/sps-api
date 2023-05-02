package com.hope.sps.customer.car;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "color", "brand", "plateNumber"})
public class CarDTO {

    private Long id;

    private String color;

    private String brand;

    private String plateNumber;
}
