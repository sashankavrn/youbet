package com.youbetcha.client;

import com.google.gson.Gson;
import com.youbetcha.model.dto.DepositDto;
import com.youbetcha.model.dto.TransactionReferenceDto;
import com.youbetcha.model.dto.WithdrawDto;
import com.youbetcha.model.payments.InitiateRequest;
import com.youbetcha.model.payments.response.InitiateResponse;
import com.youbetcha.model.response.DepositResponse;
import com.youbetcha.model.response.GmWithdrawResponse;
import com.youbetcha.model.response.WithdrawResponse;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Profile("!ci")
public class PaymentClient implements Payment {
    public static final okhttp3.MediaType JSON = okhttp3.MediaType.get("application/json; charset=utf-8");
    static final String INIT_DEPOSIT_URL = "%s/InitDeposit";
    static final String INIT_WITHDRAW_URL = "%s/InitWithdraw";
    static final String USERS_DEPOSIT_URL = "%s/ServerAPI/Deposit/%s/%s/%s";
    static final String USERS_WITHDRAW_URL = "%s/ServerAPI/Withdraw/%s/%s/%s";
    static final String PROCEED_WITHDRAW_URL = "%s/ServerAPI/ProceedWithdraw/%s/%s/%s";
    static final String ROLLBACK_WITHDRAW_URL = "%s/ServerAPI/RollbackWithdraw/%s/%s/%s";

    Gson gson = new Gson();
    private String hostName;
    private String emHostName;
    private String version;
    private String partnerId;
    private String partnerKey;
    private CustomHttpClient customHttpClient;

    public PaymentClient(CustomHttpClient customHttpClient,
                         @Value("${everymatrix.money-matrix.host-name}") String hostName,
                         @Value("${everymatrix.server-api.host-name}") String emHostName,
                         @Value("${everymatrix.server-api.version}") String version,
                         @Value("${everymatrix.server-api.partner-id}") String partnerId,
                         @Value("${everymatrix.server-api.partner-key}") String partnerKey) {
        this.customHttpClient = customHttpClient;
        this.hostName = hostName;
        this.emHostName = emHostName;
        this.version = version;
        this.partnerId = partnerId;
        this.partnerKey = partnerKey;
    }

    @Override
    public Optional<InitiateResponse> initDeposit(InitiateRequest request) {
        RequestBody body = RequestBody.Companion.create(gson.toJson(request), JSON);
        String url = String.format(INIT_DEPOSIT_URL, hostName);
        return customHttpClient.executePostCall(InitiateResponse.class, url, body,
                "Getting initiate deposit request failed with {}.",
                "Error trying to initiate deposit ");
    }

    @Override
    public Optional<InitiateResponse> initWithdraw(InitiateRequest request) {
        String url = String.format(INIT_WITHDRAW_URL, hostName);
        RequestBody body = RequestBody.Companion.create(gson.toJson(request), JSON);
        return customHttpClient.executePostCall(InitiateResponse.class, url, body,
                "Getting initiate withdraw request failed with {}.",
                "Error trying to initiate withdraw ");
    }

    @Override
    public Optional<DepositResponse> depositToGameAccount(DepositDto request) {
        String url = String.format(USERS_DEPOSIT_URL, emHostName, version, partnerId, partnerKey);
        request.setVersion(version);
        request.setPartnerID(partnerId);
        request.setPartnerKey(partnerKey);
        RequestBody body = RequestBody.Companion.create(gson.toJson(request), JSON);
        return customHttpClient.executePostCall(DepositResponse.class, url, body,
                "Depositing funds to player account request failed with {}.",
                "Error trying to deposit to game account ");
    }

    @Override
    public Optional<WithdrawResponse> withdrawFromGameAccount(WithdrawDto request) {
        String url = String.format(USERS_WITHDRAW_URL, emHostName, version, partnerId, partnerKey);
        request.setVersion(version);
        request.setPartnerID(partnerId);
        request.setPartnerKey(partnerKey);
        RequestBody body = RequestBody.Companion.create(gson.toJson(request), JSON);
        return customHttpClient.executePostCall(WithdrawResponse.class, url, body,
                "Withdrawing funds from player account request failed with {}.",
                "Error trying to withdraw from game account ");
    }

    @Override
    public Optional<GmWithdrawResponse> proceedWithdrawFromGameAccount(TransactionReferenceDto transactionReferenceDto) {
        String url = String.format(PROCEED_WITHDRAW_URL, emHostName, version, partnerId, partnerKey);
        RequestBody body = RequestBody.Companion.create(gson.toJson(transactionReferenceDto), JSON);
        return customHttpClient.executePostCall(GmWithdrawResponse.class, url, body,
                "Proceed withdrawing funds from player account request failed with {}.",
                "Error trying to proceed withdrawing from game account ");
    }

    @Override
    public Optional<GmWithdrawResponse> rollbackWithdrawFromGameAccount(TransactionReferenceDto transactionReferenceDto) {
        String url = String.format(ROLLBACK_WITHDRAW_URL, emHostName, version, partnerId, partnerKey);
        RequestBody body = RequestBody.Companion.create(gson.toJson(transactionReferenceDto), JSON);
        return customHttpClient.executePostCall(GmWithdrawResponse.class, url, body,
                "Rollback withdrawn funds from player account request failed with {}.",
                "Error trying to rollback withdrawn from game account ");
    }

}
