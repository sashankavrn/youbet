package com.youbetcha.model.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountTransactionsDto {
    private String completedFrom;
    private String completedTo;
    private String sessionId;
}
