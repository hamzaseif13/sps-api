package com.hope.sps.zone;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneUpdateRequest {

    @NotEmpty(message = "title is required")
    private String title;

    @NotEmpty(message = "fee is required")
    private Double fee;

    @NotEmpty(message = "numberOfSpaces is required")
    private Integer numberOfSpaces;

    @NotEmpty(message = "startsAt is required")
    private Time startsAt;

    @NotEmpty(message = "endsAt is required")
    private Time endsAt;
}
