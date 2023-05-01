package com.hope.sps.booking;

import com.hope.sps.UserInformation.UserInformation;
import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.customer.car.Car;
import com.hope.sps.customer.payment.wallet.Wallet;
import com.hope.sps.customer.payment.wallet.WalletRepository;
import com.hope.sps.exception.InsufficientWalletBalanceException;
import com.hope.sps.exception.InvalidResourceProvidedException;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.zone.ZoneRepository;
import com.hope.sps.zone.space.Space;
import com.hope.sps.zone.space.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final CustomerRepository customerRepository;

    private final BookingSessionRepository bookingSessionRepository;

    private final ZoneRepository zoneRepository;

    private final SpaceRepository spaceRepository;

    private final WalletRepository walletRepository;

    private final ModelMapper mapper;


    @Transactional(readOnly = true)
    public Optional<BookingDTO> getCurrentBookingSession(final UserInformation userInformation) {
        final Customer loggedInCustomer = customerRepository
                .findByUserInformationEmail(userInformation.getEmail())
                .orElseThrow();

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

        final Customer loggedInCustomer = customerRepository
                .findByUserInformationEmail(userInformation.getEmail())
                .orElseThrow();

        final var customerWallet = loggedInCustomer.getWallet();
        final long durationInMinutes = request.getDurationInMs() / 60_000;
        final double totalFeeToStrip = getTotalFeeToStrip(request.getZoneId(), customerWallet, durationInMinutes);

        walletRepository.updateBalance(customerWallet.getId(), -totalFeeToStrip);
        bookingSessionRepository.extendDuration(currentSessionId, request.getDurationInMs());
    }

    @Transactional
    public Long startNewBookingSession(final UserInformation userInformation, final BookingSessionRequest request) {
        if (spaceRepository.existsByIdAndStateIs(request.getSpaceId(), Space.State.TAKEN)) {
            throw new InvalidResourceProvidedException("space already taken");
        }

        final Customer loggedInCustomer = customerRepository
                .findByUserInformationEmail(userInformation.getEmail())
                .orElseThrow();

        if (loggedInCustomer.getActiveBookingSession() != null) {
            throw new InvalidResourceProvidedException("customer already have active booking session");
        }

        var newBookingSession = BookingSession.builder()
                .car(new Car(request.getCarId()))
                .space(new Space(request.getSpaceId()))
                .duration(request.getDurationInMs())
                .extended(false)
                .state(BookingSession.State.ACTIVE)
                .customer(loggedInCustomer)
                .build();

        final var customerWallet = loggedInCustomer.getWallet();
        final long durationInMinutes = request.getDurationInMs() / 60_000;
        final double totalFeeToStrip = getTotalFeeToStrip(request.getZoneId(), customerWallet, durationInMinutes);

        spaceRepository.updateSpaceState(request.getSpaceId(), Space.State.TAKEN);
        walletRepository.updateBalance(customerWallet.getId(), -totalFeeToStrip);

        final var createdBookingSession = bookingSessionRepository.save(newBookingSession);
        loggedInCustomer.setActiveBookingSession(createdBookingSession);

        return createdBookingSession.getId();
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

    enum BookingState {
        CURRENT, PREVIOUS
    }
}
