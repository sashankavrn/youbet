package com.youbetcha.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public enum TransactionStatus {
	PENDING,
	LIMIT_ALLOWED,
	LIMIT_NOT_ALLOWED,
	MM_LOADED,
	MM_NOT_LOADED,
	MM_SUCCESS,
	MM_ERROR,
	MM_FAILURE,
	MM_CANCEL,
	MM_PENDING,
	MM_STATUS_CHECK,
	MM_CANCELLED,
	MM_REJECTED,
	GM_SUCCESS,
	GM_FAILURE,
	GM_RETRY,
	GM_RETRIES_FAILED,
	GM_FAILED,
	GM_PASSED;
	

    @Getter
    private int value;

}
