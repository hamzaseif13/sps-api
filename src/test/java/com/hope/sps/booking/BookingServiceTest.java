package com.hope.sps.booking;

import com.hope.sps.customer.Customer;
import com.hope.sps.customer.CustomerRepository;
import com.hope.sps.customer.car.Car;
import com.hope.sps.customer.wallet.Wallet;
import com.hope.sps.exception.ResourceNotFoundException;
import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
import com.hope.sps.zone.Zone;
import com.hope.sps.zone.ZoneDTO;
import com.hope.sps.zone.ZoneRepository;
import com.hope.sps.zone.space.Space;
import com.hope.sps.zone.space.SpaceDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BookingSessionRepository bookingSessionRepository;

    @Mock
    private BookingSessionExpirationObservable bookingSessionExpirationObservable;

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private ModelMapper mapper;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void getCurrentBookingSession_WithLoggedInCustomerAndActiveBookingSession_ReturnsBookingSessionHistoryDTO() {
        // Prepare
        final String userEmail = "John@gmail.com";
        final var loggedInCustomer = createMockLoggedInCustomer();
        final var zone = createMockZone();
        final var spaceDTO = getMockSpaceDTO();
        final var zoneDTO = getMockZoneDTOFromZoneAndSpaceDTO(zone, spaceDTO);
        final var bookingSession = getMockBookingSession();
        bookingSession.getSpace().setZone(zone);
        final var bookingSessionDTO = getMockBookingSessionDTOFromBookingSessionAndSpaceDTO(bookingSession, spaceDTO);
        loggedInCustomer.setActiveBookingSession(bookingSession);

        when(customerRepository.findByUserInformationEmail(userEmail)).thenReturn(Optional.of(loggedInCustomer));
        when(mapper.map(bookingSession.getSpace().getZone(), ZoneDTO.class)).thenReturn(zoneDTO);
        when(mapper.map(bookingSession, BookingSessionDTO.class)).thenReturn(bookingSessionDTO);

        // Execute
        final Optional<BookingSessionHistoryDTO> result = bookingService.getCurrentBookingSession(userEmail);

        // Assert
        assertThat(result.isPresent()).isTrue();
        assert result.isPresent();
        assertThat(result.get().getBookingSession()).isEqualTo(bookingSessionDTO);
        assertThat(result.get().getZone()).isEqualTo(zoneDTO);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void getCurrentBookingSession_WithLoggedInCustomerAndNoActiveBookingSession_ReturnsBookingSessionHistoryDTO() {
        final String userEmail = "John@gmail.com";
        final var loggedInCustomer = createMockLoggedInCustomer();
        final var zone = createMockZone();
        final var spaceDTO = getMockSpaceDTO();
        final var zoneDTO = getMockZoneDTOFromZoneAndSpaceDTO(zone, spaceDTO);
        final var bookingSession = getMockBookingSession();
        bookingSession.getSpace().setZone(zone);
        final var bookingSessionDTO = getMockBookingSessionDTOFromBookingSessionAndSpaceDTO(bookingSession, spaceDTO);
        loggedInCustomer.setBookingHistory(Set.of(bookingSession));

        when(customerRepository.findByUserInformationEmail(userEmail)).thenReturn(Optional.of(loggedInCustomer));
        when(mapper.map(bookingSession.getSpace().getZone(), ZoneDTO.class)).thenReturn(zoneDTO);
        when(mapper.map(bookingSession, BookingSessionDTO.class)).thenReturn(bookingSessionDTO);

        // Execute
        final Optional<BookingSessionHistoryDTO> result = bookingService.getCurrentBookingSession(userEmail);

        // Assert
        assertThat(result.isPresent()).isTrue();
        assert result.isPresent();
        assertThat(result.get().getBookingSession()).isEqualTo(bookingSessionDTO);
        assertThat(result.get().getZone()).isEqualTo(zoneDTO);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void getBookingSessionHistory_WithLoggedInCustomer_ReturnsListOfBookingSessionHistoryDTO() {
        // Prepare
        final String userEmail = "John@gmail.com";
        final var loggedInCustomer = createMockLoggedInCustomer();
        final var bookingSession = getMockBookingSession();
        final var spaceDTO = getMockSpaceDTO();
        final var zone = createMockZone();
        final var zoneDTO = getMockZoneDTOFromZoneAndSpaceDTO(zone, spaceDTO);
        loggedInCustomer.setBookingHistory(Set.of(bookingSession));
        final var bookingSessionDTO = getMockBookingSessionDTOFromBookingSessionAndSpaceDTO(bookingSession, spaceDTO);

        when(customerRepository.findByUserInformationEmail(userEmail)).thenReturn(Optional.of(loggedInCustomer));
        when(mapper.map(bookingSession.getSpace().getZone(), ZoneDTO.class)).thenReturn(zoneDTO);
        when(mapper.map(bookingSession, BookingSessionDTO.class)).thenReturn(bookingSessionDTO);

        // Execute
        final List<BookingSessionHistoryDTO> result = bookingService.getBookingSessionHistory(userEmail);

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getBookingSession()).isEqualTo(bookingSessionDTO);
        assertThat(result.get(0).getZone()).isEqualTo(zoneDTO);
    }

    @Test
    void getBookingSessionHistory_WithNoLoggedInCustomer_ReturnsEmptyList() {
        // Prepare
        final String userEmail = "John@gmail.com";
        final var loggedInCustomer = createMockLoggedInCustomer();
        loggedInCustomer.setBookingHistory(null);
        when(customerRepository.findByUserInformationEmail(userEmail)).thenReturn(Optional.of(loggedInCustomer));

        // Execute
        final List<BookingSessionHistoryDTO> result = bookingService.getBookingSessionHistory(userEmail);

        // Assert
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void extendCurrentSession_validRequest_success() {
        // Prepare
        final Long currentSessionId = 1L;
        final var request = new ExtendCurrentSessionRequest(2L, 60000L);
        final String userEmail = "John@gmail.com";
        final var loggedInCustomer = createMockLoggedInCustomer();
        final var bookingSession = getMockBookingSession();

        loggedInCustomer.setActiveBookingSession(bookingSession);
        loggedInCustomer.getWallet().setBalance(BigDecimal.valueOf(100));

        when(customerRepository.findByUserInformationEmail(userEmail))
                .thenReturn(Optional.of(loggedInCustomer));

        when(bookingSessionRepository.existsBookingSessionByIdAndStateIs(currentSessionId, BookingSession.State.ACTIVE))
                .thenReturn(true);

        // Execute
        bookingService.extendCurrentSession(currentSessionId, request, userEmail);

        // Assert
        verify(customerRepository).findByUserInformationEmail(userEmail);
        verify(bookingSessionRepository).existsBookingSessionByIdAndStateIs(currentSessionId, BookingSession.State.ACTIVE);

        assertThat(loggedInCustomer.getActiveBookingSession().getExtended()).isTrue();
        assertThat(loggedInCustomer.getActiveBookingSession().getDuration()).isEqualTo(61000L);
    }

    @Test
    void extendCurrentSession_noActiveBookingSession_throwException() {
        // Prepare
        final Long currentSessionId = 1L;
        final var request = new ExtendCurrentSessionRequest(2L, 60000L);
        final String userEmail = "John@gmail.com";

        when(bookingSessionRepository.existsBookingSessionByIdAndStateIs(currentSessionId, BookingSession.State.ACTIVE))
                .thenReturn(false);

        // Execute and Assert
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> bookingService.extendCurrentSession(currentSessionId, request, userEmail));

        verify(bookingSessionRepository).existsBookingSessionByIdAndStateIs(currentSessionId, BookingSession.State.ACTIVE);
        verifyNoInteractions(customerRepository);
    }

    private Customer createMockLoggedInCustomer() {
        return new Customer(1L, "12312", new UserInformation(1L, "John", "Doe", "John@gmail.com", "password", Role.CUSTOMER), new Wallet(BigDecimal.valueOf(10.0)), null, null, Set.of(new Car()));
    }

    private Zone createMockZone() {
        return new Zone(1L, "tag", "title", 5d, 5, Time.valueOf("10:0:0"), Time.valueOf("14:0:0"), Set.of(new Space(1)), null);
    }

    private ZoneDTO getMockZoneDTOFromZoneAndSpaceDTO(final Zone zone, final SpaceDTO spaceDTO) {
        return new ZoneDTO(zone.getId(), zone.getTag(), zone.getTitle(), zone.getFee(), "amman", 5d, 6d, zone.getNumberOfSpaces(), zone.getStartsAt(), zone.getEndsAt(), 2L, List.of(spaceDTO));
    }

    private SpaceDTO getMockSpaceDTO() {
        return new SpaceDTO(1L, 5, Space.State.AVAILABLE);
    }

    private BookingSession getMockBookingSession() {
        return new BookingSession(1L, BookingSession.State.ACTIVE, false, LocalDateTime.now(), LocalDateTime.now(), 1000L, new Space(1), new Car(), null);
    }

    private BookingSessionDTO getMockBookingSessionDTOFromBookingSessionAndSpaceDTO(final BookingSession bookingSession, final SpaceDTO spaceDTO) {
        return new BookingSessionDTO(bookingSession.getId(), bookingSession.getCreatedAt(), bookingSession.getDuration(), spaceDTO, bookingSession.getCar(), bookingSession.getState(), bookingSession.getExtended());
    }
}