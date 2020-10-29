package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateMerchantReferenceFoundException extends DomainException {

    public DuplicateMerchantReferenceFoundException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
