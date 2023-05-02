package com.hope.sps.zone;

import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "fee is required")
    private Double fee;

    @NotEmpty(message = "address is required")
    private String address;

    @NotEmpty(message = "lng is required")
    private Double lng;

    @NotEmpty(message = "lat is required")
    private Double lat;

    @NotEmpty(message = "numberOfSpaces is required")
    private Integer numberOfSpaces;

    @NotEmpty(message = "startsAt is required")
    private Time startsAt;

    @NotEmpty(message = "endsAt is required")
    private Time endsAt;

}
