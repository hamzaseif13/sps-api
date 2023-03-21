package com.hope.sps.customer;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.booking.BookingSession;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String firstName;
    private String lastName;
    @OneToOne
    UserDetailsImpl userDetails;

    @OneToOne
    Wallet wallet;

    @OneToOne
    BookingSession activeBookingSession;

    @OneToMany
    @JoinColumn(name = "customer_id")
    Set<BookingSession> bookingHistory;

}
