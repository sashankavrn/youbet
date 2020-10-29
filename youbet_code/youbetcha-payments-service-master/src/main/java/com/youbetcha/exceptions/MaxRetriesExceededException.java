package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class MaxRetriesExceededException extends DomainException {

    public MaxRetriesExceededException(String message) {
        super(HttpStatus.GATEWAY_TIMEOUT, message);
    }
}
