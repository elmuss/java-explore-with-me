package ru.practicum.explorewithme.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.exception.ValidationException;
import ru.practicum.explorewithme.mapper.DateMapper;
import ru.practicum.explorewithme.model.ApiError;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(final NotFoundException e) {
        log.error("Not found error: {}", e.getMessage());
        return new ApiError(
                "NOT_FOUND",
                "The required object was not found.",
                e.getMessage(),
                DateMapper.stringFromInstant(Instant.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidation(final ValidationException e) {
        log.error("Validation error: {}", e.getMessage());
        return new ApiError(
                "BAD_REQUEST",
                "Incorrectly made request.",
                e.getMessage(),
                DateMapper.stringFromInstant(Instant.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolation(final DataIntegrityViolationException e) {
        log.error("DataIntegrityViolation error: {}", e.getMessage());
        return new ApiError(
                "CONFLICT",
                "Integrity constraint has been violated.",
                e.getMessage(),
                DateMapper.stringFromInstant(Instant.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflict(final ConflictException e) {
        log.error("Conflict error: {}", e.getMessage());
        return new ApiError(
                "CONFLICT",
                "Incorrectly made request.",
                e.getMessage(),
                DateMapper.stringFromInstant(Instant.now()));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentValidation(final MethodArgumentNotValidException e) {
        log.error("Validation error: {}", e.getMessage());
        return new ApiError(
                "BAD_REQUEST",
                "Incorrectly made request.",
                e.getMessage(),
                DateMapper.stringFromInstant(Instant.now()));
    }
}
