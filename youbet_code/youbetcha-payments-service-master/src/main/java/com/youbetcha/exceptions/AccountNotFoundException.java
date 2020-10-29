package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class AccountNotFoundException extends DomainException {

    public AccountNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
