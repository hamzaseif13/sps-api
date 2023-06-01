package com.hope.sps.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExtendedBookingSessionException extends RuntimeException {

    public ExtendedBookingSessionException(String message) {
        super(message);
    }
}
