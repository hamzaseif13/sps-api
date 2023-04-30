package com.hope.sps.booking;

import lombok.Data;

@Data
public class BookingSessionRequest {

    private Long zoneId;

    private Long spaceId;// space number todo

    private Long carId;

    private Long durationInMs;
}
