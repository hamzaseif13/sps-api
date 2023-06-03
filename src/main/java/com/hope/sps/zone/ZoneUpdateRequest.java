package com.hope.sps.zone;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneUpdateRequest {

    @NotNull(message = "title is required")
    private String title;

    @NotNull(message = "fee is required")
    private Double fee;

    @NotNull(message = "numberOfSpaces is required")
    private Integer numberOfSpaces;

    @NotNull(message = "startsAt is required")
    private Time startsAt;

    @NotNull(message = "endsAt is required")
    private Time endsAt;
}
