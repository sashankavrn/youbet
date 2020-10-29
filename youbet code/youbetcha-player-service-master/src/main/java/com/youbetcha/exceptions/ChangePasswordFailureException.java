package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class ChangePasswordFailureException extends DomainException {

    public ChangePasswordFailureException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}