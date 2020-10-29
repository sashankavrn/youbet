package com.youbetcha.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public enum TransactionType {
	DEPOSIT,
	WITHDRAW;
	
    @Getter
	@Setter
    private int value;

}
