package com.hope.sps.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidResourceException extends RuntimeException {

    public InvalidResourceException(String message) {
        super(message);
    }
}
