package com.hope.sps.booking;

import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.zone.space.SpaceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@RequiredArgsConstructor
public class BookingSessionExpirationObservable {

    private final List<BookingSession> bookingSessionObservers = new ArrayList<>();

    private final BookingSessionRepository bookingSessionRepository;

    @PostConstruct
    protected void init() {
        // get all active booking sessions from the db, and add them to the observer list
        bookingSessionObservers.addAll(
                bookingSessionRepository.getBookingSessionByStateIs(BookingSession.State.ACTIVE)
        );

        // schedule a timer tha is invoking 'notifyBookingSessionObserves() method' after 15 seconds.
        new Timer().schedule(new TimerTask() {
            @Override
            @Transactional
            public void run() {
                notifyBookingSessionObserves();
            }
        }, 0, 15000L);
    }

    // iterates over the observers and invoke their signalToInvalidateSession method

    public void notifyBookingSessionObserves() {
        // every 15s
        // for each session invokes it's invalidateBookingSession() method,
        // if it returns not empty Optional means
        // that it invalidated, and we should update it's state in the db,
        // and remove it from the bookingSessionObservers list.
        // else if it returns empty Optional means it is not invalidated, and we should ignore it
        bookingSessionObservers.removeIf(session -> {
            final Optional<BookingSession> invalidatedSession = session.invalidateBookingSession();
            invalidatedSession.ifPresent(bookingSessionRepository::save);
            return invalidatedSession.isPresent();
        });
    }

    public void attach(final BookingSession bookingSession) {
        // if to attach booking session not in the list (new one), then add it, else replace it
        bookingSessionObservers.removeIf(session1 -> session1.getId().equals(bookingSession.getId()));
        this.bookingSessionObservers.add(bookingSession);
    }
}
