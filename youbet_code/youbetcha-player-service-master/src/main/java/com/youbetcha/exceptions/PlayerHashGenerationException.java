package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class PlayerHashGenerationException extends DomainException {

    public PlayerHashGenerationException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

}
