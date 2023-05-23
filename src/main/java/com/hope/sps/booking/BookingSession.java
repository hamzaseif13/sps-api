package com.hope.sps.booking;

import com.hope.sps.customer.Customer;
import com.hope.sps.customer.car.Car;
import com.hope.sps.zone.space.Space;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking_session")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BookingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 20)
    private State state;

    @Column(name = "extended", nullable = false)
    private Boolean extended;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "duration", nullable = false)
    private Long duration;

    @ManyToOne(cascade = {CascadeType.REFRESH,CascadeType.DETACH,CascadeType.MERGE}, fetch = FetchType.EAGER)
    private Space space;

    @ManyToOne(fetch = FetchType.EAGER)
    private Car car;

    @OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Customer customer;

    public enum State {
        ARCHIVED, ACTIVE
    }

}
