package com.hope.sps.customer;

import com.hope.sps.booking.BookingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByUserInformationEmail(String email);

    Optional<Customer> findByUserInformationEmail(@Param("email") String email);

    Optional<Customer> findByActiveBookingSessionSpaceIdAndActiveBookingSessionState(@Param("spaceId") Long spaceId, @Param("state") BookingSession.State state);
}
