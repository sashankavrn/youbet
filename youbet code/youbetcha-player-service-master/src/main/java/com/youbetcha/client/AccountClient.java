package com.youbetcha.client;

import static java.lang.String.format;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.youbetcha.model.account.AccountBalanceResponse;
import com.youbetcha.model.account.AccountDetailsDto;
import com.youbetcha.model.account.AccountNotificationsDto;
import com.youbetcha.model.account.AccountTransactionsDto;
import com.youbetcha.model.account.AccountTransactionsResponse;
import com.youbetcha.model.account.AccountUpdateResponse;
import com.youbetcha.model.account.MaxDepositResponse;
import com.youbetcha.model.dto.SessionDto;
import com.youbetcha.model.response.UserAccountsResponse;

import okhttp3.MediaType;
import okhttp3.RequestBody;

@Component
@Profile("!ci")
public class AccountClient implements Account {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    static final String USERS_ACCOUNT_BALANCE_URL = "%s/ServerAPI/GetBalance/%s/%s/%s/%s/%s";
    static final String USER_ACCOUNTS_URL = "%s/ServerAPI/GetUserAccounts/%s/%s/%s/%s";
    static final String USERS_DEPOSIT_MAX_BALANCE_URL = "%s/ServerAPI/GetDepositMaxAmount/%s/%s/%s";
    static final String PLAYER_TRANSACTION_HISTORY_URL = "%s/ServerAPI/GetUserTransactionHistory/%s/%s/%s";
    static final String PLAYER_UPDATE_URL = "%s/ServerAPI/UpdateUserDetails/%s/%s/%s";

    Gson gson = new Gson();
    private String hostName;
    private String version;
    private String partnerId;
    private String partnerKey;
    private CustomHttpClient customHttpClient;

    public AccountClient(CustomHttpClient customHttpClient,
                         @Value("${everymatrix.server-api.host-name}") String hostName,
                         @Value("${everymatrix.server-api.version}") String version,
                         @Value("${everymatrix.server-api.partner-id}") String partnerId,
                         @Value("${everymatrix.server-api.partner-key}") String partnerKey) {
        this.customHttpClient = customHttpClient;
        this.hostName = hostName;
        this.version = version;
        this.partnerId = partnerId;
        this.partnerKey = partnerKey;
    }

    public Optional<AccountBalanceResponse> playersAccountBalance(String sessionId, int productCategory) {
        String accountBalanceUrl = format(USERS_ACCOUNT_BALANCE_URL, hostName, version, partnerId, partnerKey, sessionId, productCategory);

        return customHttpClient.executeGetCall(AccountBalanceResponse.class, accountBalanceUrl,
                "Account Balance request failed with {}.",
                "Error trying to get account balance ");
    }

    @Override
    public Optional<MaxDepositResponse> checkMaxDepositAmount(SessionDto sessionDto) {
        String maxDepositAmountUrl = format(USERS_DEPOSIT_MAX_BALANCE_URL, hostName, version, partnerId, partnerKey);
        RequestBody body = RequestBody.Companion.create(gson.toJson(sessionDto), JSON);
        return customHttpClient.executePostCall(MaxDepositResponse.class, maxDepositAmountUrl, body,
                "Max deposit amount request failed with {}.",
                "Error trying to get max deposit amount ");
    }

    @Override
    public Optional<UserAccountsResponse> getUserAccounts(String sessionId) {
        String userAccountsUrl = format(USER_ACCOUNTS_URL, hostName, version, partnerId, partnerKey, sessionId);
        return customHttpClient.executeGetCall(UserAccountsResponse.class, userAccountsUrl,
                "Fetching user accounts request failed with {}.",
                "Error trying to get fetch user accounts ");

    }

    public Optional<AccountTransactionsResponse> playersTransactionHistory(AccountTransactionsDto accountTransactionsDto) {
        String transactionHistoryUrl = format(PLAYER_TRANSACTION_HISTORY_URL, hostName, version, partnerId, partnerKey);
        RequestBody body = RequestBody.Companion.create(gson.toJson(accountTransactionsDto), JSON);
        return customHttpClient.executePostCall(AccountTransactionsResponse.class, transactionHistoryUrl, body,
                "Player transaction history request failed with {}.",
                "Error trying to get player transaction history ");
    }

    public Optional<AccountUpdateResponse> updatePlayerNotifications(AccountNotificationsDto dto) {
        String playerUpdateUrl = format(PLAYER_UPDATE_URL, hostName, version, partnerId, partnerKey);
        RequestBody body = RequestBody.Companion.create(gson.toJson(dto), JSON);
        return customHttpClient.executePostCall(AccountUpdateResponse.class, playerUpdateUrl, body,
                "Player update notification request failed with {}.",
                "Error trying to update player notification status");
    }

    public Optional<AccountUpdateResponse> updatePlayerDetails(AccountDetailsDto dto) {
        String playerUpdateUrl = format(PLAYER_UPDATE_URL, hostName, version, partnerId, partnerKey);
        RequestBody body = RequestBody.Companion.create(gson.toJson(dto), JSON);
        return customHttpClient.executePostCall(AccountUpdateResponse.class, playerUpdateUrl, body,
                "Player update details request failed with {}.",
                "Error trying to update player details ");
    }

}
