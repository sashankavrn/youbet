package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class InitiateTransactionException extends DomainException {

    public InitiateTransactionException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
