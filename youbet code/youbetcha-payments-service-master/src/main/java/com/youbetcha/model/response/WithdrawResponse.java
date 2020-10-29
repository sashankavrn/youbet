package com.youbetcha.model.response;

import com.youbetcha.model.ErrorData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawResponse {
    private ErrorData errorData;
    private String transactionId;
    private BigDecimal balanceAmount;
    private int transStatus;
    private int success;
    private String timestamp;
    private String version;

}


