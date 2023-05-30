package com.hope.sps.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hope.sps.customer.car.Car;
import com.hope.sps.zone.space.SpaceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "createdAt", "duration", "space", "car", "state", "extended"})
public class BookingSessionDTO {

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Long duration;

    private SpaceDTO space;

    private Car car;

    private BookingSession.State state;

    private Boolean extended;
}
