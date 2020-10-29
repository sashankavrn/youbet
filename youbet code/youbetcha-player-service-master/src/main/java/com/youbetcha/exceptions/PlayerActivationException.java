package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class PlayerActivationException extends DomainException {

    public PlayerActivationException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }

}
