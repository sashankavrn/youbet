package com.youbetcha.converter;

import com.youbetcha.model.account.AccountTransactionsResponse;
import com.youbetcha.model.dto.TransactionDto;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class WithdrawTransactionToTransactionDtoConverter implements
        Converter<AccountTransactionsResponse.WithdrawTransaction, TransactionDto> {

    private static final String WITHDRAWAL = "Withdrawal";

    @NonNull
    @Override
    public TransactionDto convert(AccountTransactionsResponse.WithdrawTransaction withdrawal) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return TransactionDto.builder()
                .transactionType(WITHDRAWAL)
                .created(LocalDateTime.parse(withdrawal.getCreated(), formatter))
                .amount(withdrawal.getDebitAmount())
                .currency(withdrawal.getDebitCurrency())
                .build();
    }
}
