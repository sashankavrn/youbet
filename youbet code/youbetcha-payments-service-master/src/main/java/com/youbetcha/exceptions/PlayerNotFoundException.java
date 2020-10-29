package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class PlayerNotFoundException extends DomainException {

    public PlayerNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
