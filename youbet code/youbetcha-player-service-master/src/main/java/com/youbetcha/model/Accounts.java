package com.youbetcha.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Accounts {
    private String balanceAmount;
    private String bonusAmount;
    private String currency;
    private String displayName;
    private long id;
    private String isBalanceAvailable;
    private String omBonusAmount;
}
