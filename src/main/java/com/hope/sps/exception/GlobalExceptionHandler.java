package com.hope.sps.exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
@Slf4j
@PropertySource("classpath:messages.properties")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {

        log.error(ex.getMessage(), ex);
        var apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList(),
                ((ServletWebRequest) request).getRequest().getServletPath()
        );

        return new ResponseEntity<>(apiError, headers, status);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiError> handleConstraintViolation(
            @NonNull ConstraintViolationException ex,
            @NonNull HttpServletRequest request
    ) {

        log.error(ex.getMessage(), ex);
        var apiError = new ApiError(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getConstraintViolations()
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .toList(),
                request.getServletPath()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseBody
    protected ResponseEntity<ApiError> handleDuplicateResource(
            DuplicateResourceException ex,
            HttpServletRequest request
    ) {

        log.error(ex.getMessage(), ex);

        var apiError = getApiError(ex, HttpStatus.CONFLICT, request);
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(InvalidResourceProvidedException.class)
    @ResponseBody
    protected ResponseEntity<ApiError> handleInvalidResourceProvided(
            InvalidResourceProvidedException ex,
            HttpServletRequest request
    ) {

        log.error(ex.getMessage(), ex);

        var apiError = getApiError(ex, HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InsufficientWalletBalanceException.class)
    @ResponseBody
    protected ResponseEntity<ApiError> handleInsufficientWalletBalance(
            InsufficientWalletBalanceException ex,
            HttpServletRequest request
    ) {

        log.error(ex.getMessage(), ex);

        var apiError = getApiError(ex, HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseBody
    protected ResponseEntity<ApiError> handleUsernameNotFound(
            UsernameNotFoundException ex,
            HttpServletRequest request
    ) {

        log.error(ex.getMessage(), ex);

        var apiError = getApiError(ex, HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    protected ResponseEntity<ApiError> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {

        log.error(ex.getMessage(), ex);

        var apiError = getApiError(ex, HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExtendedBookingSessionException.class)
    @ResponseBody
    protected ResponseEntity<ApiError> handleExtendedBookingSession(
            ExtendedBookingSessionException ex,
            HttpServletRequest request
    ) {

        log.error(ex.getMessage(), ex);

        var apiError = getApiError(ex, HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    protected ResponseEntity<ApiError> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {

        log.error(ex.getMessage(), ex);

        var apiError = getApiError(ex, HttpStatus.FORBIDDEN, request);
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    protected ResponseEntity<ApiError> handleBadCredentialsException(
            BadCredentialsException ex,
            HttpServletRequest request
    ) {

        log.error(ex.getMessage());

        var apiError = getApiError(ex, HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    protected ResponseEntity<ApiError> handleExpiredJwt(
            ExpiredJwtException ex,
            HttpServletRequest request
    ) {

        log.error(ex.getMessage());

        var apiError = getApiError(ex, HttpStatus.FORBIDDEN, request);
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    protected ResponseEntity<ApiError> handleGenericExceptionHandler(
            Exception ex,
            HttpServletRequest request
    ) {

        log.error(ex.getMessage());

        var apiError = getApiError(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiError getApiError(
            Exception ex,
            HttpStatus status,
            HttpServletRequest request
    ) {

        return new ApiError(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                List.of(ex.getMessage() == null ? "" : ex.getMessage()),
                request.getServletPath()
        );
    }
}
