package com.hope.sps.booking;

import com.hope.sps.customer.Customer;
import com.hope.sps.customer.car.Car;
import com.hope.sps.zone.space.Space;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "booking_session")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
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

    @Column(name = "ending_at", nullable = false)
    private LocalDateTime endingAt;

    @Column(name = "duration", nullable = false)
    private Long duration;

    @ManyToOne(fetch = FetchType.EAGER)
    private Space space;

    @ManyToOne(fetch = FetchType.EAGER)
    private Car car;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    @ToString.Exclude
    private Customer customer;

    public Optional<BookingSession> invalidateBookingSession() {
        //  if the duration expired, make the space's state AVAILABLE,
        // Session's state ARCHIVED, customer's current session to null
        if (LocalDateTime.now().isAfter(this.getEndingAt())) {
            this.getSpace().setState(Space.State.AVAILABLE);
            this.setState(BookingSession.State.ARCHIVED);
            this.getCustomer().setActiveBookingSession(null);
            return Optional.of(this);
        }

        return Optional.empty();
    }

    public enum State {
        ARCHIVED, ACTIVE
    }

}
