package com.youbetcha.model;

import com.youbetcha.model.payments.response.MaxDepositResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerAccount {
    private long id;
    private MaxDepositResponse maxDepositResponse;
    private int depositCount;
    private int withdrawCount;
    private boolean bonusOptIn;
}
