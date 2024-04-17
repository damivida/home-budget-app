package com.example.homebudgetapp.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class HomeBudgetException  extends Exception{
    private final Messages messages;

    private int httpStatusCode = -1;

    private String responseBody = null;

    private List<String> errors = null;


    public HomeBudgetException(Messages messages) {
        super(messages.getMessage());
        this.messages = messages;
    }

    public HomeBudgetException(Messages messages, String responseBody) {
        super(messages.getMessage());
        this.messages = messages;
        this.responseBody = responseBody;
    }

    public HomeBudgetException(String message, Messages messages, int httpStatusCode, String responseBody) {
        super(message);
        this.messages = messages;
        this.httpStatusCode = httpStatusCode;
        this.responseBody = responseBody;
    }

    public HomeBudgetException(Messages messages, List<String> errors) {
        super(messages.getMessage());
        this.messages = messages;
        this.errors = errors;
    }
}
