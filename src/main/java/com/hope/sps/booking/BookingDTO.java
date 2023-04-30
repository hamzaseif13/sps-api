package com.hope.sps.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@AllArgsConstructor
@Data
public final class BookingDTO {

    private Set<BookingSession> bookingSession;

    private String state;
}
