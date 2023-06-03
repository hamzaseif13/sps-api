package com.hope.sps.booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendCurrentSessionRequest {

    @Positive(message = "positive zoneId is required")
    private Long zoneId;

    @Positive(message = "positive durationInMs is required")
   // @Min(value = 900000, message = "durationInMs at least must be 900000, 15 min")
    private Long durationInMs;
}
