package com.hope.sps.booking;

import com.hope.sps.UserInformation.UserInformation;
import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.customer.car.Car;
import com.hope.sps.customer.payment.wallet.Wallet;
import com.hope.sps.exception.InsufficientWalletBalanceException;
import com.hope.sps.exception.InvalidResourceProvidedException;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.zone.ZoneRepository;
import com.hope.sps.zone.space.Space;
import com.hope.sps.zone.space.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final CustomerRepository customerRepository;

    private final BookingSessionRepository bookingSessionRepository;

    private final ZoneRepository zoneRepository;

    private final SpaceRepository spaceRepository;

    private final ModelMapper mapper;


    @Transactional(readOnly = true)
    public Optional<BookingDTO> getCurrentBookingSession(final UserInformation userInformation) {
        final Customer loggedInCustomer = getLoggedInCustomer(userInformation.getEmail());

        final BookingDTO bookingDTO;
        final var currentBookingSession = loggedInCustomer.getActiveBookingSession();
        final Set<BookingSession> bookingHistory = loggedInCustomer.getBookingHistory();

        if (currentBookingSession != null) {
            bookingDTO = mapper.map(currentBookingSession, BookingDTO.class);
            bookingDTO.setObjectState(BookingState.CURRENT.name());
        } else if (bookingHistory != null && !bookingHistory.isEmpty()) {
            bookingDTO = mapper.map(bookingHistory.stream().findFirst().get(), BookingDTO.class);
            bookingDTO.setObjectState(BookingState.PREVIOUS.name());
        } else {
            return Optional.empty();
        }

        return Optional.of(bookingDTO);
    }

    @Transactional
    public void extendCurrentSession(
            final Long currentSessionId,
            final ExtendCurrentSessionRequest request,
            final UserInformation userInformation
    ) {

        if (!bookingSessionRepository.existsById(currentSessionId)) {
            throw new ResourceNotFoundException("there is no active booking session");
        }

        final Customer loggedInCustomer = getLoggedInCustomer(userInformation.getEmail());

        final var customerWallet = loggedInCustomer.getWallet();
        final double totalFeeToStrip =
                getTotalFeeToStrip(request.getZoneId(), customerWallet, request.getDurationInMs() / 60_000);

        customerWallet.setBalance(customerWallet.getBalance() - totalFeeToStrip);
        loggedInCustomer.getActiveBookingSession().setExtended(true);
        loggedInCustomer.getActiveBookingSession().setDuration(request.getDurationInMs());
    }

    @Transactional
    public Long startNewBookingSession(final UserInformation userInformation, final BookingSessionRequest request) {
        // if the space with zoneId, spaceNumber and the state is taken, then the space is taken, cannot be booked
        if (spaceRepository.existsByZoneIdAndNumberAndStateIs(request.getZoneId(), request.getSpaceNumber(), Space.State.TAKEN)) {
            throw new InvalidResourceProvidedException("space already taken");
        }

        // get current logged in customer
        final Customer loggedInCustomer = getLoggedInCustomer(userInformation.getEmail());

        // if the customer already has active booking session then throw exc
        if (loggedInCustomer.getActiveBookingSession() != null) {
            throw new InvalidResourceProvidedException("customer already have active booking session");
        }

        // space which user aims to book
        var spaceToBeBooked = getSpaceByZoneIdAndSpaceNumber(request.getZoneId(), request.getSpaceNumber());

        // make new booking session
        var newBookingSession = BookingSession.builder()
                .car(new Car(request.getCarId()))
                .space(new Space(spaceToBeBooked.getId()))
                .duration(request.getDurationInMs())
                .extended(false)
                .state(BookingSession.State.ACTIVE)
                .customer(loggedInCustomer)
                .build();

        final var customerWallet = loggedInCustomer.getWallet();
        final double totalFeeToStrip =
                getTotalFeeToStrip(request.getZoneId(), customerWallet, request.getDurationInMs() / 60_000);

        spaceToBeBooked.setState(Space.State.TAKEN);
        customerWallet.setBalance(customerWallet.getBalance() - totalFeeToStrip);

        final var createdBookingSession = bookingSessionRepository.save(newBookingSession);
        loggedInCustomer.setActiveBookingSession(createdBookingSession);

        return createdBookingSession.getId();
    }

    @Transactional
    @Scheduled(fixedRate = 30000)
    public void invalidateBookingSession() {
        //get all active booking sessions
        final List<BookingSession> activeBookingSessions =
                bookingSessionRepository.getBookingSessionByStateIs(BookingSession.State.ACTIVE);

        //get current date time
        var currentLocalDateTime = LocalDateTime.now();

        // for each session if the duration expired, make the space's state AVAILABLE,
        // Session's state ARCHIVED, customer's current session to null
        activeBookingSessions.forEach(bookingSession -> {
            var durationInMinutes = bookingSession.getDuration() / 60_000;

            var toExpireLocalDateTime = bookingSession.getCreatedAt().plusMinutes(durationInMinutes);

            if (currentLocalDateTime.isAfter(toExpireLocalDateTime)) {
                bookingSession.getSpace().setState(Space.State.AVAILABLE);
                bookingSession.setState(BookingSession.State.ARCHIVED);
                bookingSession.getCustomer().setActiveBookingSession(null);
            }
        });
    }

    private double getTotalFeeToStrip(
            final Long zoneId,
            final Wallet customerWallet,
            final Long durationInMinutes
    ) {
        final double zoneFee = zoneRepository.getFeeById(zoneId);
        final double total = ((double) durationInMinutes / 60) * zoneFee;
        final double totalAfterStriping = customerWallet.getBalance() - total;

        if (totalAfterStriping < 0)
            throw new InsufficientWalletBalanceException("Insufficient balance please charge your wallet");
        return total;
    }

    private Customer getLoggedInCustomer(final String email) {
        return customerRepository.findByUserInformationEmail(email).orElseThrow();
    }

    private Space getSpaceByZoneIdAndSpaceNumber(final Long zoneId, final Integer spaceNumber) {
        return spaceRepository.findByZoneIdAndNumber(zoneId, spaceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("No space found with zoneId: [%d] and spaceNumber: [%d]"
                        .formatted(zoneId, spaceNumber))
                );
    }

    enum BookingState {
        CURRENT, PREVIOUS
    }
}
