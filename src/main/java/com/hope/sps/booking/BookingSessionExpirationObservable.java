package com.hope.sps.booking;

import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.zone.space.Space;
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

    private final SpaceRepository spaceRepository;

    private final CustomerRepository customerRepository;

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
        final var invalidatedSessions = new ArrayList<BookingSession>();

        // for each session if it invalidated then add it to the invalidatedSessions list
        for (BookingSession session : bookingSessionObservers) {
            final Optional<BookingSession> invalidatedBookingSession = session.invalidateBookingSession();
            System.out.println(session);

            invalidatedBookingSession.ifPresent(invalidatedSessions::add);
        }

        // Update all invalidated sessions, spaces, and customers in a single transaction
        if (!invalidatedSessions.isEmpty()) {
            bookingSessionRepository.saveAll(invalidatedSessions);

            final List<Space> spacesToUpdate = invalidatedSessions.stream()
                    .map(BookingSession::getSpace)
                    .toList();
            spaceRepository.saveAll(spacesToUpdate);

            final List<Customer> customersToUpdate = invalidatedSessions.stream()
                    .map(BookingSession::getCustomer)
                    .toList();
            customerRepository.saveAll(customersToUpdate);

            // Remove invalidated sessions from the observer list
            bookingSessionObservers.removeAll(invalidatedSessions);
        }
    }

    public void attach(final BookingSession bookingSession) {
        bookingSessionObservers.removeIf(session1 -> session1.getId().equals(bookingSession.getId()));
        this.bookingSessionObservers.add(bookingSession);
    }
}
