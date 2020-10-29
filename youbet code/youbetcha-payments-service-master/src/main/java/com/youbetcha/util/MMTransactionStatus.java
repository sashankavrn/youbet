package com.youbetcha.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public enum MMTransactionStatus {
    SUCCESS(1),
    ERROR(2),
    PENDINGNOTIFICATION(3),
    PENDINGCONFIRMATION(4),
    REJECTED(5),
    CANCELLED(6),
    VOIDED(7),
    PENDINGAPPROVAL(8),
    AUTHORIZED(9);

    @Getter
    @Setter
    private int value;

    MMTransactionStatus(int value) {
        this.value = value;
    }

    public int key(String passedValue) {
        return this.value;
    }
}
