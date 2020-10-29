package com.youbetcha.client;

import java.util.Optional;

import com.youbetcha.model.account.AccountBalanceResponse;
import com.youbetcha.model.account.AccountDetailsDto;
import com.youbetcha.model.account.AccountNotificationsDto;
import com.youbetcha.model.account.AccountTransactionsDto;
import com.youbetcha.model.account.AccountTransactionsResponse;
import com.youbetcha.model.account.AccountUpdateResponse;
import com.youbetcha.model.account.MaxDepositResponse;
import com.youbetcha.model.dto.SessionDto;
import com.youbetcha.model.response.UserAccountsResponse;

public interface Account {

    Optional<AccountBalanceResponse> playersAccountBalance(String sessionId, int productCategory);

    Optional<AccountTransactionsResponse> playersTransactionHistory(AccountTransactionsDto accountTransactionsDto);

    Optional<MaxDepositResponse> checkMaxDepositAmount(SessionDto sessionDto);

    Optional<UserAccountsResponse> getUserAccounts(String sessionId);

	Optional<AccountUpdateResponse> updatePlayerNotifications(AccountNotificationsDto dto);

	Optional<AccountUpdateResponse> updatePlayerDetails(AccountDetailsDto dto);

}
