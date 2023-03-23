package com.hope.sps.customer;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.booking.BookingSession;
import com.hope.sps.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "customer")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseEntity {

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Set<BookingSession> bookingHistory;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserDetailsImpl userDetails;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Wallet wallet;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private BookingSession activeBookingSession;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Set<Car> cars;
}
