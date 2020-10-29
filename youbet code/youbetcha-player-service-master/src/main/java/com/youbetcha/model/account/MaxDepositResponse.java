package com.youbetcha.model.account;

import com.youbetcha.model.ErrorData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaxDepositResponse {
    private int success;
    private String timestamp;
    private String version;
    private ErrorData errorData;
    private String currency;
    private BigDecimal amount;
}
