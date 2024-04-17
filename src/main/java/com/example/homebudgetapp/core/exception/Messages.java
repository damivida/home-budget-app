package com.example.homebudgetapp.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Messages {
    EMPTY_PARAM(HttpStatus.BAD_REQUEST, "Parameter invalid or not supplied."),
    INVALID_INPUT_TYPE(HttpStatus.BAD_REQUEST, "Input data invalid!"),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST,  "Entity not found."),
    ENTITY_EXISTS(HttpStatus.BAD_REQUEST,  "Entity already exists."),
    UNAUTHORIZED_ACTION_FOR_EXPENSE(HttpStatus.BAD_REQUEST,  "Unauthorized action for others expense."),

    INTERVAL_INVALID(HttpStatus.BAD_REQUEST, "Invalid interval."),
    INTERVAL_NOT_SPECIFIED(HttpStatus.BAD_REQUEST, "Please specify whole interval"),
    BAD_REQUEST_CONSTRAINT_ERROR(HttpStatus.BAD_REQUEST,  "Unable to perform this action there is constraint on this record.");


    private final HttpStatus httpStatus;
    private final String message;

    Messages(final HttpStatus httpStatus,  final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

}
