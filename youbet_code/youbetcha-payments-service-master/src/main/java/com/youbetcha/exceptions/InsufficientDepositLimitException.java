package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class InsufficientDepositLimitException extends DomainException {

    public InsufficientDepositLimitException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
