package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class JsonNullOrEmptyException extends DomainException {

    public JsonNullOrEmptyException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Response json null or empty");
    }

}