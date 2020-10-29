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
public class DepositTransactionToTransactionDtoConverter implements
        Converter<AccountTransactionsResponse.DepositTransaction, TransactionDto> {

    private static final String DEPOSIT = "Deposit";

    @NonNull
    @Override
    public TransactionDto convert(AccountTransactionsResponse.DepositTransaction deposit) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return TransactionDto.builder()
                .transactionType(DEPOSIT)
                .created(LocalDateTime.parse(deposit.getCreated(), formatter))
                .amount(deposit.getCreditAmount())
                .currency(deposit.getCreditCurrency())
                .build();
    }
}
