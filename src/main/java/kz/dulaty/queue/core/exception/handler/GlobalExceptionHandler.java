package kz.dulaty.queue.core.exception.handler;

import jakarta.persistence.EntityNotFoundException;
import kz.dulaty.queue.core.common.enums.CommonExceptionCode;
import kz.dulaty.queue.core.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.hibernate.PropertyValueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({NotFoundException.class, NoSuchElementException.class})
    protected ResponseEntity<Object> handleNoSuchElementException(Exception ex, WebRequest request) {
        log.error("not found exception: code = {}", ex.getLocalizedMessage(), ex);

        ApiErrorDto errorDto = new ApiErrorDto(ZonedDateTime.now(), NOT_FOUND,
                CommonExceptionCode.OBJECT_NOT_FOUND.name(), ex.getMessage(), getPath(request));

        return handleExceptionInternal(ex, errorDto, HttpHeaders.EMPTY, NOT_FOUND, request);
    }

    @ExceptionHandler({BadRequestException.class})
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        log.error("not found exception: code = {}", ex.getLocalizedMessage(), ex);

        ApiErrorDto errorDto = new ApiErrorDto(ZonedDateTime.now(), BAD_REQUEST,
                CommonExceptionCode.DATA_VALIDATION_FAILED.name(), ex.getMessage(), getPath(request));

        return handleExceptionInternal(ex, errorDto, HttpHeaders.EMPTY, BAD_REQUEST, request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(Exception ex, WebRequest request) {
        log.error("Entity not found exception: code = {}", ex.getLocalizedMessage(), ex);

        ApiErrorDto errorDto = new ApiErrorDto(ZonedDateTime.now(), NOT_FOUND,
                CommonExceptionCode.OBJECT_NOT_FOUND.name(), ex.getMessage(), getPath(request));

        return handleExceptionInternal(ex, errorDto, HttpHeaders.EMPTY, NOT_FOUND, request);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistsException(Exception ex, WebRequest request) {
        log.error("User Already Exists Exception: code = {}", ex.getLocalizedMessage(), ex);

        ApiErrorDto errorDto = new ApiErrorDto(ZonedDateTime.now(), CONFLICT,
                CommonExceptionCode.USER_ALREADY_EXISTS.name(), ex.getMessage(), getPath(request));

        return handleExceptionInternal(ex, errorDto, HttpHeaders.EMPTY, CONFLICT, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(Exception ex, WebRequest request) {
        log.error("Authentication Exception: code = {}", ex.getLocalizedMessage(), ex);

        ApiErrorDto errorDto = new ApiErrorDto(ZonedDateTime.now(), UNAUTHORIZED,
                CommonExceptionCode.ACCESS_DENIED.name(), ex.getMessage(), getPath(request));

        return handleExceptionInternal(ex, errorDto, HttpHeaders.EMPTY, UNAUTHORIZED, request);
    }

    @ExceptionHandler(CustomAuthenticationException.class)
    protected ResponseEntity<Object> handleCustomAuthenticationException(Exception ex, WebRequest request) {
        log.error("Authentication Exception: code = {}", ex.getLocalizedMessage(), ex);

        ApiErrorDto errorDto = new ApiErrorDto(ZonedDateTime.now(), UNAUTHORIZED,
                CommonExceptionCode.ACCESS_DENIED.name(), ex.getMessage(), getPath(request));

        return handleExceptionInternal(ex, errorDto, HttpHeaders.EMPTY, UNAUTHORIZED, request);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, PropertyValueException.class})
    protected ResponseEntity<Object> handleDataIntegrityViolationException(Exception ex, WebRequest request) {
        log.error("Data Integrity Violation Exception: code = {}", ex.getLocalizedMessage(), ex);

        ApiErrorDto errorDto = new ApiErrorDto(ZonedDateTime.now(), BAD_REQUEST,
                CommonExceptionCode.DATA_VALIDATION_FAILED.name(), ex.getMessage(), getPath(request));

        return handleExceptionInternal(ex, errorDto, HttpHeaders.EMPTY, BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("Method Argument Not Valid Exception: code = {}", ex.getLocalizedMessage(), ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiErrorListDto errorDto = new ApiErrorListDto(ZonedDateTime.now(), BAD_REQUEST, CommonExceptionCode.DATA_VALIDATION_FAILED.name(), errors, getPath(request));

        return handleExceptionInternal(ex, errorDto, HttpHeaders.EMPTY, BAD_REQUEST, request);
    }

    private String getPath(WebRequest request) {
        return (request instanceof ServletWebRequest)
                ? ((ServletWebRequest) request).getRequest().getRequestURI()
                : request.getContextPath();
    }
}
