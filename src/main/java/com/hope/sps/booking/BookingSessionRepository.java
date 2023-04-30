package com.hope.sps.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingSessionRepository extends JpaRepository<BookingSession, Long> {

    @Query("UPDATE BookingSession b SET b.extended=true, b.duration = b.duration+:durationInMin WHERE b.id=:BS_ID")
    void extendDuration(@Param("BS_ID") Long bookingSessionId,@Param("durationInMin") Long durationInMinutes);
}
