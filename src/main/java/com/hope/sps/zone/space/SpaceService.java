package com.hope.sps.zone.space;

import com.hope.sps.booking.BookingSession;
import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        final Customer occupiedSpaceCustomer = customerRepository
                .findByActiveBookingSessionSpaceIdAndActiveBookingSessionState(spaceId, BookingSession.State.ACTIVE).orElseThrow();

        final String customerFirstName = occupiedSpaceCustomer.getUserInformation().getFirstName();
        final String customerLastName = occupiedSpaceCustomer.getUserInformation().getLastName();
        final String customerName = String.format("%s %s", customerFirstName, customerLastName);

        final BookingSession customerActiveBookingSession = occupiedSpaceCustomer.getActiveBookingSession();

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
