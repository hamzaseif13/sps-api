package com.hope.sps.zone;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneRegistrationRequest {

    @NotEmpty(message = "tag is required")
    private String tag;

    @NotEmpty(message = "title is required")
    private String title;

    @NotNull(message = "fee is required")
    private Double fee;

    @NotEmpty(message = "address is required")
    private String address;

    @NotNull(message = "lng is required")
    private Double lng;

    @NotNull(message = "lat is required")
    private Double lat;

    @NotNull(message = "numberOfSpaces is required")
    private Integer numberOfSpaces;

    @NotNull(message = "startsAt is required")
    private Time startsAt;

    @NotNull(message = "endsAt is required")
    private Time endsAt;

}
