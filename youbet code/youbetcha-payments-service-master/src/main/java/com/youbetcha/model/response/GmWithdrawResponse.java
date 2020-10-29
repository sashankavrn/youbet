package com.youbetcha.model.response;

import com.youbetcha.model.ErrorData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GmWithdrawResponse {
    private ErrorData errorData;
    private String transactionId;
    private int transStatus;
    private int success;
    private String version;
    private String timestamp;
}
