package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class DepositBonusException extends DomainException {

    public DepositBonusException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
