package com.hope.sps.booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingSessionRequest {

    @Positive(message = "positive zoneId is required")
    @NotEmpty(message = "zoneId is mandatory")
    private Long zoneId;

    @Positive(message = "positive spaceId is required")
    @NotEmpty(message = "spaceId is mandatory")
    private Long spaceId;
    // space number todo
    //send me space id if you dont wont then send zone id with space number
    @Positive(message = "positive carId is required")
    @NotEmpty(message = "carId is mandatory")
    private Long carId;

    @Positive(message = "positive durationInMs is required")
    @Min(value = 900000, message = "durationInMs at least must be 900000, 15 min")
    private Long durationInMs;
}
