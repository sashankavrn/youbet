package com.youbetcha.model.event;

import com.youbetcha.model.payments.response.MaxDepositResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AccountsEvent {
    private long creditAccountId;
    private MaxDepositResponse maxDepositResponse;
    private String merchantReference;
}
