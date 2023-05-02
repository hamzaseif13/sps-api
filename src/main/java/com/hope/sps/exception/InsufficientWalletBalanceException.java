package com.hope.sps.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InsufficientWalletBalanceException extends RuntimeException {
    public InsufficientWalletBalanceException(String message) {
        super(message);
    }
}
