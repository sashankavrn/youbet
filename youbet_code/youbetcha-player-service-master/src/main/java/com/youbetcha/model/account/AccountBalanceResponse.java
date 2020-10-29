package com.youbetcha.model.account;

import java.util.List;

import com.youbetcha.model.ErrorData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class AccountBalanceResponse {
    private ErrorData errorData;
    private Short success;
    private String timestamp;
    private List<Wallet> wallets;

    @Builder
    @lombok.Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Wallet {
        private Double bonusMoney;
        private String bonusMoneyCurrency;
        private Double lockedMoney;
        private String lockedMoneyCurrency;
        private Double realMoney;
        private String realMoneyCurrency;
        private String name;

        public Double getAllMoney() {
        	return bonusMoney + realMoney;
        }
    }
    

}