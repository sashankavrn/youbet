package com.youbetcha.model.account;

import com.youbetcha.model.ErrorData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class AccountTransactionsResponse {
    private ErrorData errorData;
    private Short success;
    private String timestamp;
    private List<DepositTransaction> depositTransList;
    private List<WithdrawTransaction> withdrawTransList;

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DepositTransaction {
        private String created;
        private Long creditAccountId;
        private Double creditAmount;
        private String creditCurrency;
        private String creditDisplayName;
        private String debitDisplayName;
        private String debitPayItemName;
        private String debitPayItemVendorName;
        private Long id;
        private Short status;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WithdrawTransaction {
        private String created;
        private String creditDisplayName;
        private String creditPayItemName;
        private String creditPayItemVendorName;
        private Long debitAccountId;
        private Double debitAmount;
        private String debitCurrency;
        private String debitDisplayName;
        private Long id;
        private Short status;
    }
}