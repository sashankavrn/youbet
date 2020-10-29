package com.youbetcha.model.payments;

import lombok.Getter;

public enum InitiateType {
    WITHDRAW,
    DEPOSIT;

    @Getter
    private String value;
}
