package com.example.homebudgetapp.core.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(HomeBudgetException.class)
    public ResponseEntity<Object> handleHomeBudgetException(HomeBudgetException ex) {
        ErrorResponse responseBase = new ErrorResponse(ex.getMessages());

        if (Objects.nonNull(ex.getErrors())) {
            responseBase.setErrors(ex.getErrors());
        }
        return new ResponseEntity<>(responseBase, new HttpHeaders(), responseBase.getStatus());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleArgumentException(final IllegalArgumentException e) {
        logException(e);
        final ErrorResponse errorResponse = new ErrorResponse(Messages.EMPTY_PARAM);
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(final EntityNotFoundException ex) {
        logException(ex);
        final ErrorResponse responseBase = new ErrorResponse(HttpStatus.NOT_FOUND, 404, ex.getMessage());
        return new ResponseEntity<>(responseBase, new HttpHeaders(), responseBase.getStatus());
    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<ErrorResponse> handleTypeMismatch(final MethodArgumentTypeMismatchException ex) {
        logException(ex);
        final Messages messages = Messages.INVALID_INPUT_TYPE;

        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(messages.getMessage());
        errorResponse.setStatus(messages.getHttpStatus());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            final DataIntegrityViolationException e) {

        final ErrorResponse errorResponse = new ErrorResponse(Messages.BAD_REQUEST_CONSTRAINT_ERROR);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(final NoSuchElementException ex) {
        logException(ex);

        final ErrorResponse responseBase = new ErrorResponse(HttpStatus.NOT_FOUND, 404, ex.getMessage());

        return new ResponseEntity<>(responseBase, new HttpHeaders(), responseBase.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatusCode status,
            final WebRequest request) {
        logException(ex);

        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method not supported. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

        final ErrorResponse responseBase = new ErrorResponse(HttpStatus.BAD_REQUEST, 405, builder.toString());
        return new ResponseEntity<>(responseBase, new HttpHeaders(), responseBase.getStatus());
    }

    private void logException(Throwable e) {
        log.error("Unhandled exception!", e);
    }

}
