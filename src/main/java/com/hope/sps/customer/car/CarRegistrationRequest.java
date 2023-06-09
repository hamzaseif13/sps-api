package com.hope.sps.customer.car;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarRegistrationRequest {

    @Length(min = 3, max = 20, message = "color must be between 3 and 20 char")
    @NotNull(message = "car's color is required")
    private String color;

    @Length(min = 3, max = 50, message = "brand must be between 3 and 50 char")
    @NotNull(message = "car's brand is required")
    private String brand;

    @Length(min = 1, max = 15, message = "plateNumber must be between 1 and 15 char")
    @NotNull(message = "car's plateNumber is required")
    private String plateNumber;

}
