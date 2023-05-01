package com.hope.sps.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingSessionRepository extends JpaRepository<BookingSession, Long> {

    @Modifying
    @Query("UPDATE BookingSession b SET b.extended=true, b.duration = b.duration+:durationInMs WHERE b.id=:BS_ID")
    void extendDuration(@Param("BS_ID") Long bookingSessionId, @Param("durationInMs") Long durationInMS);
}
