package com.hope.sps.booking;

import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.customer.car.Car;
import com.hope.sps.customer.wallet.Wallet;
import com.hope.sps.exception.ExtendedBookingSessionException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final CustomerRepository customerRepository;

    private final BookingSessionRepository bookingSessionRepository;

    private final ZoneRepository zoneRepository;

    private final SpaceRepository spaceRepository;

    private final BookingSessionExpirationObservable sessionExpirationObservable;

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

        // if there is a current booking session then, map it to BookingSessionHistoryDTO and return it,
        // else get the last booking session from the history
        // and map it to BookingSessionHistoryDTO and return it.
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

        // if there is no active booking session then throw exception
        throwExceptionIfNoActiveBookingSession(currentSessionId);

        // get logged in customer
        final Customer loggedInCustomer = getLoggedInCustomer(userInformation.getEmail());

        // if the active booking session is already extended, then throw exception
        throwExceptionIfActiveBookingSessionIsExtended(loggedInCustomer);

        // get logged in customer's wallet
        final var customerWallet = loggedInCustomer.getWallet();

        // calculate and get total fee to strip after extend the current session
        // note an exception will be thrown if insufficient balance
        final BigDecimal totalFeeToStrip = calculateTotalFeeToStrip(
                request.getZoneId(),
                customerWallet,
                request.getDurationInMs() / 60_000
        );

        // strip totalFeeToStrip from customer wallet's balance
        customerWallet.setBalance(customerWallet.getBalance().subtract(totalFeeToStrip));

        // get Active booking session for logged-in customer
        final var activeBookingSession = loggedInCustomer.getActiveBookingSession();

        // mark it as extended and increase its duration and update endingAt,
        final long updatedDurationInMs = activeBookingSession.getDuration() + request.getDurationInMs();
        activeBookingSession.setExtended(true);
        activeBookingSession.setDuration(updatedDurationInMs);
        activeBookingSession.setEndingAt(
                LocalDateTime.now().plusMinutes(updatedDurationInMs / 60_000)
        );
        sessionExpirationObservable.attach(activeBookingSession);
    }


    @Transactional
    public Long startNewBookingSession(final String customerEmail, final BookingSessionRequest request) {

        // if the space with zoneId, spaceNumber and the state is taken, then the space is taken, cannot be booked
        throwExceptionIfToBookSpaceIsTaken(request);

        // get current logged in customer
        final Customer loggedInCustomer = getLoggedInCustomer(customerEmail);

        // if the customer already has active booking session then throw exc
        throwExceptionIfThereIsAlreadyActiveSession(loggedInCustomer);

        // if the customer didn't specify a car or specified a car but not belong to him, then throw exception
        throwExceptionIfNoValidCarSpecified(loggedInCustomer.getCars(), request.getCarId());

        // space which user aims to book
        final var spaceToBeBooked = getSpaceByZoneIdAndSpaceNumber(request.getZoneId(), request.getSpaceNumber());

        // assemble new booking session to be persisted
        final var newBookingSession = BookingSession.builder()
                .car(new Car(request.getCarId()))
                .space(spaceToBeBooked)
                .duration(request.getDurationInMs())
                .extended(false)
                .state(BookingSession.State.ACTIVE)
                .customer(loggedInCustomer)
                .build();

        // get logged in customer's wallet
        final var customerWallet = loggedInCustomer.getWallet();

        // calculate and get total fee to strip after starting the session
        // note an exception will be thrown if insufficient balance
        final BigDecimal totalFeeToStrip = calculateTotalFeeToStrip(
                request.getZoneId(),
                customerWallet,
                request.getDurationInMs() / 60_000
        );

        // mark the space as taken
        spaceToBeBooked.setState(Space.State.TAKEN);

        // strip totalFeeToStrip from customer wallet's balance
        customerWallet.setBalance(customerWallet.getBalance().subtract(totalFeeToStrip));

        // set ending at
        newBookingSession.setEndingAt(
                LocalDateTime.now().plusMinutes(newBookingSession.getDuration() / 60_000)
        );

        // save the booking session and assign it to the customer, then return its id
        final var createdBookingSession = bookingSessionRepository.save(newBookingSession);
        loggedInCustomer.setActiveBookingSession(createdBookingSession);
        sessionExpirationObservable.attach(createdBookingSession);
        return createdBookingSession.getId();
    }

    private BigDecimal calculateTotalFeeToStrip(
            final Long zoneId,
            final Wallet customerWallet,
            final Long durationInMinutes
    ) {

        // get zone's fee
        final var zoneFee = BigDecimal.valueOf(zoneRepository.getFeeById(zoneId));

        // convert the duration in minutes to hours
        final var durationInHours = BigDecimal.valueOf(durationInMinutes)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);

        // calculate the total fee by multiplying durationInHours and zoneFee
        final BigDecimal totalFee = zoneFee.multiply(durationInHours);

        // calculates customer wallet's balance after stripping
        final BigDecimal customerWalletBalanceAfterStripping = customerWallet.getBalance().subtract(totalFee);

        // throw exception if there is no sufficient wallet balance after stripping
        if (customerWalletBalanceAfterStripping.compareTo(BigDecimal.ZERO) == 0)
            throw new InsufficientWalletBalanceException("Insufficient balance please charge your wallet");

        return totalFee;
    }

    /***************** HELPER METHODS *********************/

    // Retrieving Customer by its email, from the DB.
    // Guarantee to return a customer because only authenticated and authorized customer can reach here
    private Customer getLoggedInCustomer(final String email) {
        return customerRepository.findByUserInformationEmail(email).orElseThrow();
    }

    // map the booking session with the associated zone to DTOs, prepare the BookingSessionHistoryDTO and return it
    private BookingSessionHistoryDTO prepareBookingSessionHistoryDTO(final BookingSession currentBookingSession) {
        final var bookingSessionDTO = mapper.map(currentBookingSession, BookingSessionDTO.class);
        final var zoneDTO = mapper.map(currentBookingSession.getSpace().getZone(), ZoneDTO.class);
        return new BookingSessionHistoryDTO(bookingSessionDTO, zoneDTO);
    }

    private Space getSpaceByZoneIdAndSpaceNumber(final Long zoneId, final Integer spaceNumber) {
        return spaceRepository.findByZoneIdAndNumber(zoneId, spaceNumber)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No space found with zoneId: [%d] and spaceNumber: [%d]"
                                .formatted(zoneId, spaceNumber))
                );
    }

    private void throwExceptionIfNoActiveBookingSession(final Long currentSessionId) {
        if (!bookingSessionRepository.existsBookingSessionByIdAndStateIs(currentSessionId, BookingSession.State.ACTIVE)) {
            throw new ResourceNotFoundException("there is no active booking session");
        }
    }

    private void throwExceptionIfActiveBookingSessionIsExtended(Customer loggedInCustomer) {
        if (loggedInCustomer.getActiveBookingSession().getExtended()) {
            throw new ExtendedBookingSessionException("this booking session already extended, cannot be extended anymore");
        }
    }

    private void throwExceptionIfToBookSpaceIsTaken(final BookingSessionRequest request) {
        if (spaceRepository.existsByZoneIdAndNumberAndStateIs(request.getZoneId(), request.getSpaceNumber(), Space.State.TAKEN)) {
            throw new InvalidResourceProvidedException("space already taken");
        }
    }

    private void throwExceptionIfThereIsAlreadyActiveSession(Customer loggedInCustomer) {
        if (loggedInCustomer.getActiveBookingSession() != null) {
            throw new InvalidResourceProvidedException("customer already have active booking session");
        }
    }

    private void throwExceptionIfNoValidCarSpecified(final Set<Car> loggedInCustomerCars, final Long specifiedCarId) {
        if (loggedInCustomerCars.isEmpty() || !loggedInCustomerCars.contains(new Car(specifiedCarId)))
            throw new InvalidResourceProvidedException("you need to choose your car");
    }

    public Long getCounter() {
        return bookingSessionRepository.count();
    }
}
