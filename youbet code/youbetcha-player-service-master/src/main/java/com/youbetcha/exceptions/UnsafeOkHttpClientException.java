package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

import java.security.GeneralSecurityException;

public class UnsafeOkHttpClientException extends DomainException {

    public UnsafeOkHttpClientException(GeneralSecurityException message) {
        super(HttpStatus.UNAUTHORIZED, message.getMessage());
    }
}