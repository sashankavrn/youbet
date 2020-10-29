package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class RegistrationFieldUnavailableException extends DomainException {

    public RegistrationFieldUnavailableException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
