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

    @Length(min = 3, max = 20, message = "invalid color size")
    @NotNull(message = "car's color is required")
    private String color;

    @Length(min = 3, max = 50, message = "invalid brand size")
    @NotNull(message = "car's brand is required")
    private String brand;

    @Length(min = 1, max = 15, message = "invalid plateNumber size")
    @NotNull(message = "car's plateNumber is required")
    private String plateNumber;

}
