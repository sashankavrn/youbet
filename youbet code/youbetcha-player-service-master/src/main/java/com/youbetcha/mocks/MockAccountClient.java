package com.youbetcha.mocks;

import com.youbetcha.client.Account;
import com.youbetcha.model.Accounts;
import com.youbetcha.model.ErrorData;
import com.youbetcha.model.account.AccountBalanceResponse;
import com.youbetcha.model.account.AccountDetailsDto;
import com.youbetcha.model.account.AccountNotificationsDto;
import com.youbetcha.model.account.AccountTransactionsDto;
import com.youbetcha.model.account.AccountTransactionsResponse;
import com.youbetcha.model.account.AccountUpdateResponse;
import com.youbetcha.model.account.MaxDepositResponse;
import com.youbetcha.model.dto.SessionDto;
import com.youbetcha.model.response.UserAccountsResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

@Component
@Profile("ci")
public class MockAccountClient implements Account {
    private static final String TIMESTAMP = "2019-02-12 15:12:06";

    public static ErrorData generateSuccessfulAccountErrorData() {
        return ErrorData.builder()
                .errorCode((short) 0)
                .errorDetails(Collections.emptyList())
                .errorMessage("")
                .logId(0)
                .build();
    }

    @Override
    public Optional<AccountBalanceResponse> playersAccountBalance(String sessionId, int productCategory) {
        return Optional.of(getAccountBalance());
    }

    @Override
    public Optional<AccountTransactionsResponse> playersTransactionHistory(AccountTransactionsDto accountTransactionsDto) {
        return Optional.of(getAccountTransactions());
    }

    @Override
    public Optional<MaxDepositResponse> checkMaxDepositAmount(SessionDto sessionDto) {
        return Optional.of(getMaxDepositAmountResponse());
    }

    @Override
    public Optional<UserAccountsResponse> getUserAccounts(String sessionId) {
        return Optional.of(getUserAccounts());
    }

    private UserAccountsResponse getUserAccounts() {
        return UserAccountsResponse.builder()
                .accounts(Collections.singletonList(Accounts.builder()
                        .balanceAmount("10").bonusAmount("0").currency("USD").displayName("player1")
                        .id(123321L).isBalanceAvailable("1").omBonusAmount("12").build()))
                .errorData(generateSuccessfulAccountErrorData())
                .isNegativeBalance("0")
                .success(1)
                .timestamp(TIMESTAMP)
                .version("1")
                .build();
    }

    private MaxDepositResponse getMaxDepositAmountResponse() {
        return MaxDepositResponse.builder()
                .amount(BigDecimal.TEN)
                .currency("USD")
                .timestamp(TIMESTAMP)
                .success(1)
                .version("1.0")
                .errorData(generateSuccessfulAccountErrorData()).build();
    }

    private AccountTransactionsResponse getAccountTransactions() {
        AccountTransactionsResponse.AccountTransactionsResponseBuilder accountTransactionsResponse = AccountTransactionsResponse.builder()
                .errorData(generateSuccessfulAccountErrorData())
                .withdrawTransList(Collections.singletonList(AccountTransactionsResponse.WithdrawTransaction.builder()
                        .created("2020-08-02")
                        .creditDisplayName("Credit display name")
                        .creditPayItemName("Credit Pay Item")
                        .creditPayItemVendorName("Credit Vendor")
                        .debitAccountId(9223372036854775807L)
                        .debitAmount(123.67)
                        .debitCurrency("EUR")
                        .debitDisplayName("Debit display name")
                        .id(922337203685477580L)
                        .status(Short.parseShort("0"))
                        .build()))
                .depositTransList(Collections.singletonList(AccountTransactionsResponse.DepositTransaction.builder()
                        .created("2020-08-01")
                        .creditAccountId(9223372036854775807L)
                        .creditAmount(1234.54)
                        .creditCurrency("EUR")
                        .creditDisplayName("Transfer credit")
                        .debitDisplayName("Debit display name")
                        .debitPayItemName("Debit Pay Item")
                        .debitPayItemVendorName("Debit Vendor")
                        .id(9223372036854775807L)
                        .status(Short.parseShort("0"))
                        .build()));
        return accountTransactionsResponse.build();
    }

    private AccountBalanceResponse getAccountBalance() {

        return AccountBalanceResponse.builder()
                .errorData(generateSuccessfulAccountErrorData())
                .wallets(Collections.singletonList(AccountBalanceResponse.Wallet.builder()
                        .realMoney(345d)
                        .realMoneyCurrency("GBP")
                        .bonusMoney(300d)
                        .bonusMoneyCurrency("GBP")
                        .lockedMoney(100d)
                        .lockedMoneyCurrency("GBP")
                        .build()))
                .success((short) 1)
                .timestamp(TIMESTAMP)
                .build();
    }

	@Override
	public Optional<AccountUpdateResponse> updatePlayerNotifications(AccountNotificationsDto dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<AccountUpdateResponse> updatePlayerDetails(AccountDetailsDto dto) {
		// TODO Auto-generated method stub
		return null;
	}
}
