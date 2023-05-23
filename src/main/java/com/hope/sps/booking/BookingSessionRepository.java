package com.hope.sps.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingSessionRepository extends JpaRepository<BookingSession, Long> {

    List<BookingSession> getBookingSessionByStateIs(BookingSession.State state);
}
