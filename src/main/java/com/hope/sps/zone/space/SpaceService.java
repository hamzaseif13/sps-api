package com.hope.sps.zone.space;

import com.hope.sps.booking.BookingSession;
import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.exception.InvalidResourceProvidedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpaceService {

    private final SpaceRepository spaceRepository;

    private final CustomerRepository customerRepository;

    public SpaceAvailabilityResponse checkAvailability(final SpaceAvailabilityRequest request) {

        if (spaceRepository.existsByZoneIdAndNumberAndStateIs(
                request.zoneId(),
                request.spaceNumber(),
                Space.State.AVAILABLE)
        ) {
            return new SpaceAvailabilityResponse(true, "space available");
        }

        return new SpaceAvailabilityResponse(false, "space not available");
    }

    public OccupiedSpaceDTO getOccupiedSpaceInformation(final Long spaceId) {
        final Optional<Customer> occupiedSpaceCustomer = customerRepository
                .findByActiveBookingSessionSpaceIdAndActiveBookingSessionState(spaceId, BookingSession.State.ACTIVE);

        if (occupiedSpaceCustomer.isEmpty())
            throw new InvalidResourceProvidedException("this space is not occupied");

        final Customer customer = occupiedSpaceCustomer.get();
        final String customerFirstName = customer.getUserInformation().getFirstName();
        final String customerLastName = customer.getUserInformation().getLastName();
        final String customerName = String.format("%s %s", customerFirstName, customerLastName);

        final BookingSession customerActiveBookingSession = customer.getActiveBookingSession();

        return new OccupiedSpaceDTO(
                customerName,
                customerActiveBookingSession.getCar().getBrand(),
                customerActiveBookingSession.getCar().getColor(),
                customerActiveBookingSession.getCreatedAt(),
                customerActiveBookingSession.getDuration(),
                customerActiveBookingSession.getExtended()
        );
    }
}
