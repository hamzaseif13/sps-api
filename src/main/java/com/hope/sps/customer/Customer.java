package com.hope.sps.customer;

import com.hope.sps.UserDetails.Role;
import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.booking.BookingSession;
import com.hope.sps.customer.car.Car;
import com.hope.sps.customer.payment.Wallet;
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
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class Customer extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserDetailsImpl userDetails;

    @Column(name = "phone_number",length = 20)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Wallet wallet;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private BookingSession activeBookingSession;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Set<BookingSession> bookingHistory;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Set<Car> cars;

    public Customer(UserDetailsImpl userDetails, Wallet wallet, String phoneNumber) {
        this.userDetails = userDetails;
        this.wallet = wallet;
        this.phoneNumber = phoneNumber;
        userDetails.setRole(Role.CUSTOMER);
    }
}
