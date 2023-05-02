package com.hope.sps.booking;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.hope.sps.customer.car.Car;
import com.hope.sps.zone.space.Space;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "createdAt", "duration", "space", "car", "state", "extended", "objectState"})
public class BookingDTO {
    //i can do better if i have zone tag
    private Long id;

    private LocalDateTime createdAt;

    private Long duration;

    private Space space;

    private Car car;

    private BookingSession.State state;

    private Boolean extended;

    private String objectState;
}
