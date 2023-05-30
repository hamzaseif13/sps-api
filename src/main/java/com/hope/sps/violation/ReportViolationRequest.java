package com.hope.sps.violation;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportViolationRequest {

    @Length(min = 1, max = 15, message = "invalid plateNumber size")
    @NotNull(message = "car's plateNumber is required")
    private String plateNumber;

    @Length(min = 3, max = 20, message = "invalid color size")
    @NotNull(message = "car's color is required")
    private String carColor;

    @Length(min = 3, max = 50, message = "invalid brand size")
    @NotNull(message = "car's brand is required")
    private String carBrand;

    @Length(min = 20, max = 255, message = "invalid description size")
    @NotNull(message = "violation details is required")
    private String details;

    @NotNull(message = "image is required")
    private String imageUrl;
}
