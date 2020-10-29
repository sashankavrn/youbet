package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class SignatureHashingFailureException extends DomainException {

    public SignatureHashingFailureException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
