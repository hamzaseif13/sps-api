package com.hope.sps.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/booking")
//@PreAuthorize("hasRole('CUSTOMER')")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("current")
    public ResponseEntity<BookingDTO> getCurrentBookingSessionForLoggedInUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        final Optional<BookingDTO> bookingDTO = bookingService.getCurrentBookingSession(authentication);

        return bookingDTO.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @PostMapping
    public ResponseEntity<Long> startNewBookingSession(
            @RequestBody
            @Valid BookingSessionRequest request
    ) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        final Long id = bookingService.startNewBookingSession(authentication, request);

        return ResponseEntity.ok(id);
    }

    @PatchMapping("{sessionId}/extend")
    public ResponseEntity<Void> extendCurrentSession(
            @PathVariable("sessionId") Long currentSessionId,
            @RequestBody Long durationInMS
    ) {
        bookingService.extendCurrentSession(currentSessionId, durationInMS);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //session trigger invalidate end

    //history from newest to oldest

    //add balance for wallet ,int custId, balance int

    //add car
}
