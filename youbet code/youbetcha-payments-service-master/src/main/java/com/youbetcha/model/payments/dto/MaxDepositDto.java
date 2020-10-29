package com.youbetcha.model.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaxDepositDto {
    private String sessionId;
    private String userId;
    private String currency;
}
