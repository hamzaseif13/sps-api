package com.hope.sps.exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@PropertySource("classpath:messages.properties")
public class ResponseEntityExceptionHandler {

    @Value("${exc.ConstraintViolationException}")
    private String CONSTRAINT_VIOLATION_ERROR_MSG;

    @Value("${exc.DuplicateResourceException}")
    private String DUPLICATE_RESOURCE_ERROR_MSG;

    @Value("${exc.InvalidResourceException}")
    private String INVALID_RESOURCE_ERROR_MSG;

    @Value("${exc.UsernameNotFound}")
    private String USERNAME_NOT_FOUND;


    @ExceptionHandler(value = DuplicateResourceException.class)
    protected ResponseEntity<ApiError> handelDuplicateResource(DuplicateResourceException ex) {

        return ResponseEntityBuilder.build(
                new ApiError(
                        LocalDateTime.now(),
                        HttpStatus.CONFLICT,
                        DUPLICATE_RESOURCE_ERROR_MSG,
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {

        return ResponseEntityBuilder.build(
                new ApiError(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST,
                        CONSTRAINT_VIOLATION_ERROR_MSG,
                        ex.getConstraintViolations()
                                .stream()
                                .map(ConstraintViolation::getMessage)
                                .toList()
                                .toString()
                ));
    }

    @ExceptionHandler(InvalidResourceException.class)
    protected ResponseEntity<ApiError> handleInvalidResource(InvalidResourceException ex){

        return ResponseEntityBuilder.build(
                new ApiError(
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST,
                        INVALID_RESOURCE_ERROR_MSG,
                        ex.getMessage()
                ));
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<ApiError> usernameNotFound(UsernameNotFoundException ex){

        return ResponseEntityBuilder.build(
                new ApiError(
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND,
                        INVALID_RESOURCE_ERROR_MSG,
                        ex.getMessage()
                ));
    }


    private static class ResponseEntityBuilder {
        private static ResponseEntity<ApiError> build(ApiError apiError) {
            return new ResponseEntity<>(apiError, apiError.status());
        }
    }
}
