package com.hope.sps.booking;

import com.hope.sps.user_information.UserInformation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/booking")
//@PreAuthorize("hasAuthority('CUSTOMER')")
public class BookingController {

    private final BookingService bookingService;

    // Endpoint for retrieving current BookingSessionHistoryDTO (200 OK)
    // or the latest one if no current exists (200 OK)
    // or (204 No Content) if no data found
    @GetMapping("current")
    public ResponseEntity<BookingSessionHistoryDTO> getCurrentBookingSessionForLoggedInUser(
            @AuthenticationPrincipal
            UserInformation loggedInUser
    ) {
        final var bookingDTO = bookingService.getCurrentBookingSession(loggedInUser.getEmail());

        return bookingDTO.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    // Endpoint for retrieving all Booking Sessions (200 OK)
    // or (204 No Content) if no data found
    @GetMapping("history")
    public ResponseEntity<List<BookingSessionHistoryDTO>> getBookingSessionHistoryForLoggedInUser(
            @AuthenticationPrincipal
            UserInformation loggedInUser
    ) {
        final var bookingSessionHistoryDTOList = bookingService.getBookingSessionHistory(loggedInUser.getEmail());

        if (bookingSessionHistoryDTOList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseEntity.ok(bookingSessionHistoryDTOList);
    }


    @PostMapping
    public ResponseEntity<Long> startNewBookingSession(
            @AuthenticationPrincipal
            UserInformation loggedInUser,
            @RequestBody @Valid
            BookingSessionRequest request
    ) {
        final Long id = bookingService.startNewBookingSession(loggedInUser, request);

        return ResponseEntity.ok(id);
    }

    @PatchMapping("{sessionId}/extend")
    public ResponseEntity<Void> extendCurrentSession(
            @PathVariable("sessionId")
            @Validated @Positive
            Long currentSessionId,
            @RequestBody @Valid
            ExtendCurrentSessionRequest request,
            @AuthenticationPrincipal
            UserInformation loggedInUser
    ) {
        bookingService.extendCurrentSession(currentSessionId, request, loggedInUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
