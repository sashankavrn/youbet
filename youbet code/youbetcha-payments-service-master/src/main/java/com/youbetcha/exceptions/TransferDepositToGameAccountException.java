package com.youbetcha.exceptions;

import org.springframework.http.HttpStatus;

public class TransferDepositToGameAccountException extends DomainException {
    public TransferDepositToGameAccountException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
