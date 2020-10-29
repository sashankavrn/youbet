package com.youbetcha.service;

import com.youbetcha.client.Account;
import com.youbetcha.exceptions.AccountException;
import com.youbetcha.model.account.AccountBalanceResponse;
import com.youbetcha.model.account.AccountDetailsDto;
import com.youbetcha.model.account.AccountNotificationsDto;
import com.youbetcha.model.account.AccountTransactionsDto;
import com.youbetcha.model.account.AccountTransactionsResponse;
import com.youbetcha.model.account.AccountUpdateResponse;
import com.youbetcha.model.account.MaxDepositResponse;
import com.youbetcha.model.dto.SessionDto;
import com.youbetcha.model.dto.TransactionDto;
import com.youbetcha.model.response.UserAccountsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    Account accountClient;

    @Qualifier("mvcConversionService")
    @Autowired
    ConversionService converter;

    public AccountBalanceResponse accountBalance(String sessionId, int productType) {
        return accountClient.playersAccountBalance(sessionId, productType)
                .orElseThrow(() -> new AccountException("Account balance lookup error"));
    }

    public List<TransactionDto> accountTransactions(String sessionId, AccountTransactionsDto accountTransactionsDto) {

        accountTransactionsDto.setSessionId(sessionId);
        AccountTransactionsResponse transactionsResponse = accountClient.playersTransactionHistory(accountTransactionsDto)
                .orElseThrow(() -> new AccountException("Account transaction lookup error"));

        List<TransactionDto> transactions = new ArrayList<>();

        if (transactionsResponse == null) throw new AccountException("Account transaction lookup error");

        if (transactionsResponse.getDepositTransList() != null && !transactionsResponse.getDepositTransList().isEmpty()) {
            for (AccountTransactionsResponse.DepositTransaction deposit : transactionsResponse.getDepositTransList()) {
                transactions.add(converter.convert(deposit, TransactionDto.class));
            }
        }

        if (transactionsResponse.getWithdrawTransList() != null && !transactionsResponse.getWithdrawTransList().isEmpty()) {
            for (AccountTransactionsResponse.WithdrawTransaction withdrawal : transactionsResponse.getWithdrawTransList()) {
                transactions.add(converter.convert(withdrawal, TransactionDto.class));
            }
        }

        transactions.sort(Comparator.comparing(TransactionDto::getCreated));
        return transactions;
    }

    public MaxDepositResponse checkMaxDepositAmount(SessionDto sessionDto) {
        return accountClient.checkMaxDepositAmount(sessionDto)
                .orElseThrow(() -> new AccountException("Max deposit amount lookup error"));
    }

    public UserAccountsResponse getUserAccounts(String sessionId) {
        return accountClient.getUserAccounts(sessionId)
                .orElseThrow(() -> new AccountException("Fetching user accounts error"));
    }

	public AccountUpdateResponse updateNotifications(String sessionId, AccountNotificationsDto dto) {
		dto.setSessionId(sessionId);
        AccountUpdateResponse updateResponse = accountClient.updatePlayerNotifications(dto)
                .orElseThrow(() -> new AccountException("Account notifications update error"));
		return updateResponse;
	}
	
	public AccountUpdateResponse updateDetails(String sessionId, AccountDetailsDto dto) {
		dto.setSessionId(sessionId);
        AccountUpdateResponse updateResponse = accountClient.updatePlayerDetails(dto)
                .orElseThrow(() -> new AccountException("Account details update error"));
		return updateResponse;
	}
}
