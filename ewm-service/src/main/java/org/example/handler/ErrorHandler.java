package org.example.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.ConflictException;
import org.example.exception.NotFoundException;
import org.example.exception.ValidationException;
import org.example.mapper.DateMapper;
import org.example.model.ApiError;
import org.example.model.ErrorStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(final NotFoundException e) {
        log.error("Not found error: {}", e.getMessage());
        return new ApiError(
                ErrorStatus.NOT_FOUND,
                "The required object was not found.",
                e.getMessage(),
                DateMapper.stringFromInstant(Instant.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(final ValidationException e) {
        log.error("Validation error: {}", e.getMessage());
        return new ApiError(
                ErrorStatus.BAD_REQUEST,
                "Incorrectly made request.",
                e.getMessage(),
                DateMapper.stringFromInstant(Instant.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolation(final DataIntegrityViolationException e) {
        log.error("DataIntegrityViolation error: {}", e.getMessage());
        return new ApiError(
                ErrorStatus.CONFLICT,
                "Integrity constraint has been violated.",
                e.getMessage(),
                DateMapper.stringFromInstant(Instant.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(final ConflictException e) {
        log.error("Conflict error: {}", e.getMessage());
        return new ApiError(
                ErrorStatus.CONFLICT,
                "Incorrectly made request.",
                e.getMessage(),
                DateMapper.stringFromInstant(Instant.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentValidation(final MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage());
        return new ApiError(
                ErrorStatus.BAD_REQUEST,
                "Incorrectly made request.",
                e.getMessage(),
                DateMapper.stringFromInstant(Instant.now()));
    }
}
