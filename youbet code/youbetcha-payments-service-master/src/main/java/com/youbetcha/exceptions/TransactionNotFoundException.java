package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class TransactionNotFoundException extends DomainException {

    public TransactionNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
