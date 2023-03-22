package com.hope.sps.customer;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.booking.BookingSession;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String firstName;
    private String lastName;

    @OneToOne(cascade = CascadeType.PERSIST,fetch =FetchType.EAGER)
    UserDetailsImpl userDetails;

    @OneToOne(fetch =FetchType.EAGER)
    Wallet wallet;

    @OneToOne(fetch =FetchType.EAGER)
    BookingSession activeBookingSession;

    @OneToMany(fetch =FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    Set<BookingSession> bookingHistory;

}
