package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class UnsuccessfulPlayerLogoutException extends DomainException {

    public UnsuccessfulPlayerLogoutException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
