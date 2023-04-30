package com.hope.sps.booking;

import com.hope.sps.customer.car.Car;
import com.hope.sps.model.BaseEntity;
import com.hope.sps.zone.space.Space;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "booking_session")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
public class BookingSession extends BaseEntity {

    // select * from booking session where space id = spaceId, join user and car
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 20)
    private State state = State.ACTIVE;

    @Column(name = "extended", nullable = false)
    private Boolean extended = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "duration", nullable = false)
   // @Size(min = 1000 * 60 * 30)//30 min todo
    private Long duration;

    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Space space;

    @ManyToOne(fetch = FetchType.EAGER)
    private Car car;

    public enum State {
        ARCHIVED, ACTIVE
    }

}
