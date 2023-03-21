package com.hope.sps.booking;

import com.hope.sps.zone.space.Space;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

@Entity
public class BookingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreationTimestamp
    LocalDateTime createdAt;

    Long duration;

    @ManyToOne
    Space space;

    @Enumerated(EnumType.STRING)
    State state;

    Boolean extended;
    public static enum State{
        ARCHIVED,ACTIVE
    }

}
