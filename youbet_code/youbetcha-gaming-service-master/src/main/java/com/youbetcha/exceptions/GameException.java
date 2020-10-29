package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class GameException extends DomainException {

    public GameException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
