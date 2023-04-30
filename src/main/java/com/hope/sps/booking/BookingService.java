package com.hope.sps.booking;

import com.hope.sps.UserDetails.UserDetailsImpl;
import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.customer.car.Car;
import com.hope.sps.exception.InsufficientWalletBalanceException;
import com.hope.sps.zone.ZoneRepository;
import com.hope.sps.zone.space.Space;
import com.hope.sps.zone.space.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final CustomerRepository customerRepository;

    private final BookingSessionRepository bookingSessionRepository;

    private final ZoneRepository zoneRepository;

    private final SpaceRepository spaceRepository;


    public Optional<BookingDTO> getCurrentBookingSession(final Authentication authentication) {
        final Customer loggedInCustomer = getLoggedInCustomer(authentication);
        final BookingDTO bookingDTO;

        if (loggedInCustomer.getActiveBookingSession() != null) {
            bookingDTO = new BookingDTO(Set.of(loggedInCustomer.getActiveBookingSession()), BookingState.CURRENT.name());
        } else {
            if (loggedInCustomer.getBookingHistory() == null || loggedInCustomer.getBookingHistory().isEmpty()) {
                return Optional.empty();
            } else {//modify to last boooking session todo
                bookingDTO = new BookingDTO(loggedInCustomer.getBookingHistory(), BookingState.PREVIOUS.name());
            }
        }//duration in ms

        return Optional.of(bookingDTO);
    }

    public void extendCurrentSession(final Long currentSessionId, final Long durationInMS) {
        if (bookingSessionRepository.existsById(currentSessionId)) {
            final Long durationInMinutes = durationInMS / 60_000;
            bookingSessionRepository.extendDuration(currentSessionId, durationInMinutes);
        }
    }

    public Long startNewBookingSession(final Authentication authentication, final BookingSessionRequest request) {
        if(spaceRepository.existsByIdAndStateIs(request.getSpaceId(),Space.State.TAKEN)){


        }

        final Customer loggedInCustomer = getLoggedInCustomer(authentication);
        final long durationInMinutes = request.getDurationInMs() / 60_000;

        var newBookingSession = BookingSession.builder()
                .car(new Car(request.getCarId()))
                .space(new Space(request.getSpaceId(),Space.State.TAKEN))
                .duration(durationInMinutes)
                .extended(false)
                .state(BookingSession.State.ACTIVE)
                .build();

        final double zoneFee = zoneRepository.getFeeById(request.getZoneId());
        final double total = ((double) durationInMinutes / 60) * zoneFee;
        final double totalAfterStriping = loggedInCustomer.getWallet().getBalance() - total;

        if (totalAfterStriping < 0)
            throw new InsufficientWalletBalanceException("Insufficient balance please charge your wallet");

        loggedInCustomer.getWallet().setBalance(totalAfterStriping);
        loggedInCustomer.setActiveBookingSession(newBookingSession);

        return bookingSessionRepository.save(newBookingSession).getId();
    }


    private Customer getLoggedInCustomer(Authentication authentication) {
        final UserDetailsImpl customerUserDetails = (UserDetailsImpl) authentication.getPrincipal();
        return customerRepository
                .findByUserDetailsEmail(customerUserDetails.getEmail())
                .orElseThrow();
    }

    enum BookingState {
        CURRENT, PREVIOUS
    }
}
