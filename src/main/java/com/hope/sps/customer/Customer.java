package com.hope.sps.customer;

import com.hope.sps.booking.BookingSession;
import com.hope.sps.customer.car.Car;
import com.hope.sps.customer.wallet.Wallet;
import com.hope.sps.user_information.UserInformation;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserInformation userInformation;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Wallet wallet;

    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private BookingSession activeBookingSession;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    @OrderBy(value = "createdAt DESC")
    private Set<BookingSession> bookingHistory;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Set<Car> cars;

    public Customer(UserInformation userInformation, Wallet wallet, String phoneNumber) {
        this.userInformation = userInformation;
        this.wallet = wallet;
        this.phoneNumber = phoneNumber;
    }

    public void addCar(final Car car) {
        if (car == null)
            cars = new HashSet<>();

        cars.add(car);
    }
}
