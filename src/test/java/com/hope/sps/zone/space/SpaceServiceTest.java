package com.hope.sps.zone.space;

import com.hope.sps.booking.BookingSession;
import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.customer.car.Car;
import com.hope.sps.exception.InvalidResourceProvidedException;
import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpaceServiceTest {

    @Mock
    private SpaceRepository spaceRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private SpaceService spaceService;

    @Test
    void testCheckAvailability_SpaceIsAvailable_ShouldReturnAvailableResponse() {
        // Prepare
        final SpaceAvailabilityRequest request = new SpaceAvailabilityRequest(1L, 1);
        when(spaceRepository.existsByZoneIdAndNumberAndStateIs(1L, 1, Space.State.AVAILABLE)).thenReturn(true);

        // Execute
        final SpaceAvailabilityResponse response = spaceService.checkAvailability(request);

        // Assert
        assertThat(response.isAvailable()).isTrue();
        assertThat(response.message()).isEqualTo("space available");
        verify(spaceRepository, times(1))
                .existsByZoneIdAndNumberAndStateIs(1L, 1, Space.State.AVAILABLE);
    }

    @Test
    void testCheckAvailability_SpaceIsNotAvailable_ShouldReturnNotAvailableResponse() {
        // Prepare
        final SpaceAvailabilityRequest request = new SpaceAvailabilityRequest(1L, 1);
        when(spaceRepository.existsByZoneIdAndNumberAndStateIs(1L, 1, Space.State.AVAILABLE)).thenReturn(false);

        // Execute
        final SpaceAvailabilityResponse response = spaceService.checkAvailability(request);

        // Assert
        assertThat(response.isAvailable()).isFalse();
        assertThat(response.message()).isEqualTo("space not available");
        verify(spaceRepository, times(1))
                .existsByZoneIdAndNumberAndStateIs(1L, 1, Space.State.AVAILABLE);
    }

    @Test
    void testGetOccupiedSpaceInformation_SpaceIsOccupied_ShouldReturnOccupiedSpaceDTO() {
        // Prepare
        final Long spaceId = 1L;
        final var customer = createCustomerWithActiveBookingSession(spaceId);
        when(customerRepository.findByActiveBookingSessionSpaceIdAndActiveBookingSessionState(spaceId, BookingSession.State.ACTIVE))
                .thenReturn(Optional.of(customer));

        // Execute
        final OccupiedSpaceDTO result = spaceService.getOccupiedSpaceInformation(spaceId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getCustomerName()).isEqualTo("John Doe");
        assertThat(result.getCarBrand()).isEqualTo("BMW");
        assertThat(result.getCarColor()).isEqualTo("RED");
        assertThat(result.getSessionDuration()).isEqualTo(60L);
        assertThat(result.isSessionExtended()).isFalse();
        verify(customerRepository, times(1))
                .findByActiveBookingSessionSpaceIdAndActiveBookingSessionState(spaceId, BookingSession.State.ACTIVE);
    }

    @Test
    void testGetOccupiedSpaceInformation_SpaceIsNotOccupied_ShouldThrowException() {
        // Arrange
        final Long spaceId = 1L;
        when(customerRepository.findByActiveBookingSessionSpaceIdAndActiveBookingSessionState(spaceId, BookingSession.State.ACTIVE))
                .thenReturn(Optional.empty());

        // Act and Assert
        assertThatExceptionOfType(InvalidResourceProvidedException.class)
                .isThrownBy(() -> spaceService.getOccupiedSpaceInformation(spaceId));

        verify(customerRepository, times(1))
                .findByActiveBookingSessionSpaceIdAndActiveBookingSessionState(spaceId, BookingSession.State.ACTIVE);
    }

    private Customer createCustomerWithActiveBookingSession(Long spaceId) {
        final var car = new Car(1L, "RED", "BMW", "416313");
        final var activeBookingSession = new BookingSession(spaceId, BookingSession.State.ACTIVE, false, LocalDateTime.now(), LocalDateTime.MAX, 60L, new Space(1), car, null);
        activeBookingSession.setState(BookingSession.State.ACTIVE);
        activeBookingSession.setCreatedAt(LocalDateTime.now());
        activeBookingSession.setDuration(60L);
        activeBookingSession.setExtended(false);
        return new Customer(1L, "131310", new UserInformation("John", "Doe", "John@gmail.com", "password", Role.CUSTOMER), null, activeBookingSession, Set.of(activeBookingSession), Set.of(car));
    }
}
