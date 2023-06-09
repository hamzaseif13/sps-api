package com.hope.sps.booking;


import com.hope.sps.customer.car.Car;
import com.hope.sps.user_information.Role;
import com.hope.sps.user_information.UserInformation;
import com.hope.sps.zone.ZoneDTO;
import com.hope.sps.zone.space.Space;
import com.hope.sps.zone.space.SpaceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    private BookingSessionHistoryDTO bookingSessionHistoryDTO;

    @BeforeEach
    void setUp() {
        final var spaceDTO = new SpaceDTO(1L, 1, Space.State.TAKEN);
        final var car = new Car(1L, "RED", "BMW", "1234567898765");
        bookingSessionHistoryDTO = new BookingSessionHistoryDTO(
                new BookingSessionDTO(1L, LocalDateTime.now(), 900000L, spaceDTO, car, BookingSession.State.ACTIVE, false),
                new ZoneDTO(1L, "TAG", "TITLE", 0.5d, "ADDRESS", 20D, 50D, 5, Time.valueOf("8:8:8"), Time.valueOf("18:18:18"), 2L, null)
        );
    }

    @Test
    void getCurrentBookingSessionForLoggedInUser_WithExistingBooking_ReturnsBookingSessionDTO() {
        // Prepare
        final var loggedInUser = new UserInformation("John", "Doe", "customer@example.com", "password", Role.CUSTOMER);

        when(bookingService.getCurrentBookingSession(loggedInUser.getEmail())).thenReturn(Optional.of(bookingSessionHistoryDTO));

        // Execute
        final ResponseEntity<BookingSessionHistoryDTO> response = bookingController.getCurrentBookingSessionForLoggedInUser(loggedInUser);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(bookingSessionHistoryDTO);

        verify(bookingService).getCurrentBookingSession(loggedInUser.getEmail());
    }

    @Test
    void getCurrentBookingSessionForLoggedInUser_WithNoExistingBooking_ReturnsNoContent() {
        // Prepare
        final var loggedInUser = new UserInformation("John", "Doe", "customer@example.com", "password", Role.CUSTOMER);
        when(bookingService.getCurrentBookingSession(loggedInUser.getEmail())).thenReturn(Optional.empty());

        // Execute
        final ResponseEntity<BookingSessionHistoryDTO> response = bookingController.getCurrentBookingSessionForLoggedInUser(loggedInUser);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();

        verify(bookingService).getCurrentBookingSession(loggedInUser.getEmail());
    }

    @Test
    void getBookingSessionHistoryForLoggedInUser_WithExistingBooking_ReturnsBookingSessionHistoryDTOList() {
        // Prepare
        final var loggedInUser = new UserInformation("John", "Doe", "customer@example.com", "password", Role.CUSTOMER);
        when(bookingService.getBookingSessionHistory(loggedInUser.getEmail())).thenReturn(List.of(bookingSessionHistoryDTO));

        // Execute
        final ResponseEntity<List<BookingSessionHistoryDTO>> response = bookingController.getBookingSessionHistoryForLoggedInUser(loggedInUser);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).get(0)).isEqualTo(bookingSessionHistoryDTO);

        verify(bookingService).getBookingSessionHistory(loggedInUser.getEmail());
    }

    @Test
    void getBookingSessionHistoryForLoggedInUser_WithNoBooking_ReturnsNoContent() {
        // Prepare
        final var loggedInUser = new UserInformation("John", "Doe", "customer@example.com", "password", Role.CUSTOMER);
        when(bookingService.getBookingSessionHistory(loggedInUser.getEmail())).thenReturn(new ArrayList<>());

        // Execute
        final ResponseEntity<List<BookingSessionHistoryDTO>> response = bookingController.getBookingSessionHistoryForLoggedInUser(loggedInUser);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();

        verify(bookingService).getBookingSessionHistory(loggedInUser.getEmail());
    }

    @Test
    void startNewBookingSession_WithValidRequest_ReturnsBookingSessionId() {
        // Prepare
        final var loggedInUser = new UserInformation("John", "Doe", "customer@example.com", "password", Role.CUSTOMER);
        final var request = new BookingSessionRequest(1L, 1, 1L, 900000L);
        final Long bookingSessionId = 1L;
        when(bookingService.startNewBookingSession(loggedInUser.getEmail(), request)).thenReturn(bookingSessionId);

        // Execute
        final ResponseEntity<Long> response = bookingController.startNewBookingSession(loggedInUser, request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(bookingSessionId);

        verify(bookingService).startNewBookingSession(loggedInUser.getEmail(), request);
    }

    @Test
    void extendCurrentSession_WithValidRequest_ReturnsOk() {
        // Prepare
        final var loggedInUser = new UserInformation("John", "Doe", "customer@example.com", "password", Role.CUSTOMER);
        final Long sessionId = 1L;
        final var request = new ExtendCurrentSessionRequest(1L, 900000L);

        // Execute
        final ResponseEntity<Void> response = bookingController.extendCurrentSession(sessionId, request, loggedInUser);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(bookingService).extendCurrentSession(sessionId, request, loggedInUser);
    }
}
