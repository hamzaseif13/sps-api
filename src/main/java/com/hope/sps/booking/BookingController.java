package com.hope.sps.booking;

import com.hope.sps.UserInformation.UserInformation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/booking")
@PreAuthorize("hasAuthority('CUSTOMER')")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("current")
    public ResponseEntity<BookingDTO> getCurrentBookingSessionForLoggedInUser(
            @AuthenticationPrincipal
            UserInformation loggedInUser
    ) {
        final Optional<BookingDTO> bookingDTO = bookingService.getCurrentBookingSession(loggedInUser);

        return bookingDTO.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
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

    //session trigger invalidate end

    //history from newest to oldest

    //add balance for wallet ,int custId, balance int

    //add car
}
