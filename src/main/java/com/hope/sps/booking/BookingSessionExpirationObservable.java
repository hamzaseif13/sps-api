package com.hope.sps.booking;

import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.zone.space.SpaceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
            public void run() {
                notifyBookingSessionObserves();
            }
        }, 0, 15000L);
    }

    // iterates over the observers and invoke their signalToInvalidateSession method

    public void notifyBookingSessionObserves() {
        final Iterator<BookingSession> bookingSessionIterator = bookingSessionObservers.iterator();

        // for each session try to invalidate it, if invalidation success then updated space,session and customer information
        while (bookingSessionIterator.hasNext()) {
            final BookingSession session = bookingSessionIterator.next();
            final BookingSession invalidatedBookingSession = session.invalidateBookingSession()
                    .orElse(null);

            if (invalidatedBookingSession != null) {
                bookingSessionRepository.save(invalidatedBookingSession);
                spaceRepository.save(invalidatedBookingSession.getSpace());
                customerRepository.save(invalidatedBookingSession.getCustomer());
                bookingSessionIterator.remove();
            }
        }
    }

    public void attach(final BookingSession bookingSession) {
        bookingSessionObservers.removeIf(session1 -> session1.equals(bookingSession));
        bookingSessionObservers.add(bookingSession);
        this.bookingSessionObservers.add(bookingSession);
    }
}
