package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class AccountException extends DomainException {

    public AccountException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
