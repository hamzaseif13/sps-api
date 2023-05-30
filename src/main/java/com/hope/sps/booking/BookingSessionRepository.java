package com.hope.sps.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingSessionRepository extends JpaRepository<BookingSession, Long> {

    List<BookingSession> getBookingSessionByStateIs(BookingSession.State state);

    boolean existsBookingSessionByIdAndStateIs(@Param("id") Long sessionId, @Param("state") BookingSession.State state);
}
