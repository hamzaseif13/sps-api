package com.hope.sps.booking;

import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.customer.car.Car;
import com.hope.sps.customer.payment.wallet.Wallet;
import com.hope.sps.exception.InsufficientWalletBalanceException;
import com.hope.sps.exception.InvalidResourceProvidedException;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.user_information.UserInformation;
import com.hope.sps.zone.ZoneDTO;
import com.hope.sps.zone.ZoneRepository;
import com.hope.sps.zone.space.Space;
import com.hope.sps.zone.space.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final CustomerRepository customerRepository;

    private final BookingSessionRepository bookingSessionRepository;

    private final ZoneRepository zoneRepository;

    private final SpaceRepository spaceRepository;

    private final ModelMapper mapper;


    // returns BookingSessionHistoryDTO represents current booking session with its zone
    // or last booking session with its zone if no current session exists
    // for logged-in customer, else empty optional
    @Transactional(readOnly = true)
    public Optional<BookingSessionHistoryDTO> getCurrentBookingSession(final String userEmail) {
        // get logged in customer from DB
        final Customer loggedInCustomer = getLoggedInCustomer(userEmail);

        // init BookingSessionHistoryDTO for the controller
        final BookingSessionHistoryDTO bookingSessionHistoryDTO;

        // get current booking session of logged in customer
        final var currentBookingSession = loggedInCustomer.getActiveBookingSession();

        // get booking session history of logged in customer
        final Set<BookingSession> bookingHistory = loggedInCustomer.getBookingHistory();

        // if there is no current booking session nor history (or empty history), then return empty optional
        if (currentBookingSession == null && (bookingHistory == null || bookingHistory.isEmpty())) {
            return Optional.empty();
        }

        // if there is a current booking session then, map it to BookingSessionHistoryDTO and return it.
        // "orElse(null)" will never get executed
        bookingSessionHistoryDTO = prepareBookingSessionHistoryDTO(
                Objects.requireNonNullElseGet(
                        currentBookingSession,
                        () -> bookingHistory.stream().findFirst().orElse(null))
        );
        return Optional.of(bookingSessionHistoryDTO);
    }

    @Transactional(readOnly = true)
    public List<BookingSessionHistoryDTO> getBookingSessionHistory(final String userEmail) {
        // get logged in customer from DB
        final Customer loggedInCustomer = getLoggedInCustomer(userEmail);

        // get booking session history of logged in customer
        final Set<BookingSession> bookingSessionHistory = loggedInCustomer.getBookingHistory();
        System.err.println(bookingSessionHistory);
        if (bookingSessionHistory == null)
            return Collections.emptyList();

        // for each booking session, map it to BookingSessionHistoryDTO and return all of them as a list.
        return bookingSessionHistory.stream().map(this::prepareBookingSessionHistoryDTO).toList();
    }


    @Transactional
    public void extendCurrentSession(
            final Long currentSessionId,
            final ExtendCurrentSessionRequest request,
            final UserInformation userInformation
    ) {

        if (!bookingSessionRepository.existsBookingSessionByIdAndStateIs(currentSessionId, BookingSession.State.ACTIVE)) {
            throw new ResourceNotFoundException("there is no active booking session");
        }

        final Customer loggedInCustomer = getLoggedInCustomer(userInformation.getEmail());

        final var customerWallet = loggedInCustomer.getWallet();
        final double totalFeeToStrip =
                getTotalFeeToStrip(request.getZoneId(), customerWallet, request.getDurationInMs() / 60_000);

        customerWallet.setBalance(customerWallet.getBalance().subtract(new BigDecimal(totalFeeToStrip)));
        loggedInCustomer.getActiveBookingSession().setExtended(true);
        loggedInCustomer.getActiveBookingSession().setDuration(request.getDurationInMs() + loggedInCustomer.getActiveBookingSession().getDuration());
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
        customerWallet.setBalance(customerWallet.getBalance().subtract(new BigDecimal(totalFeeToStrip)));
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
        final double totalAfterStriping = customerWallet.getBalance().subtract(new BigDecimal(total)).doubleValue();

        if (totalAfterStriping < 0)
            throw new InsufficientWalletBalanceException("Insufficient balance please charge your wallet");
        return total;
    }

    /***************** HELPER METHODS *********************/

    // Retrieving Customer by its email, from the DB.
    // Guarantee to return a customer because only authenticated and authorized customer can reach here
    private Customer getLoggedInCustomer(final String email) {
        return customerRepository.findByUserInformationEmail(email).orElseThrow();
    }

    // map the booking session with the associated zone to DTOs, prepare the BookingSessionHistoryDTO and return it
    private BookingSessionHistoryDTO prepareBookingSessionHistoryDTO(final BookingSession currentBookingSession) {
        final BookingSessionDTO bookingSessionDTO = mapper.map(currentBookingSession, BookingSessionDTO.class);
        final ZoneDTO zoneDTO = mapper.map(currentBookingSession.getSpace().getZone(), ZoneDTO.class);
        return new BookingSessionHistoryDTO(bookingSessionDTO, zoneDTO);
    }

    private Space getSpaceByZoneIdAndSpaceNumber(final Long zoneId, final Integer spaceNumber) {
        return spaceRepository.findByZoneIdAndNumber(zoneId, spaceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("No space found with zoneId: [%d] and spaceNumber: [%d]"
                        .formatted(zoneId, spaceNumber))
                );
    }
}
