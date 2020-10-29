package com.youbetcha.mocks;

import com.youbetcha.client.Payment;
import com.youbetcha.model.ErrorData;
import com.youbetcha.model.dto.DepositDto;
import com.youbetcha.model.dto.TransactionReferenceDto;
import com.youbetcha.model.dto.WithdrawDto;
import com.youbetcha.model.payments.InitiateRequest;
import com.youbetcha.model.payments.response.InitiateResponse;
import com.youbetcha.model.response.DepositResponse;
import com.youbetcha.model.response.GmWithdrawResponse;
import com.youbetcha.model.response.WithdrawResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

@Component
@Profile("ci")
public class MockPaymentClient implements Payment {

    public static ErrorData generateSuccessfulAccountErrorData() {
        return ErrorData.builder()
                .errorCode((short) 0)
                .errorDetails(Collections.emptyList())
                .errorMessage("")
                .logId(0)
                .build();
    }

    @Override
    public Optional<InitiateResponse> initDeposit(InitiateRequest request) {
        return Optional.of(generateResponse());
    }

    private InitiateResponse generateResponse() {
        return InitiateResponse.builder()
                .CashierUrl("youbetcha.com")
                .ResponseCode(1)
                .ResponseDisplayText("Success")
                .ResponseMessage("Success")
                .Signature("GoOxaCXSgdOAwActSgsCOViT")
                .build();
    }

    @Override
    public Optional<DepositResponse> depositToGameAccount(DepositDto request) {
        return Optional.of(generateDepositResponse());
    }

    @Override
    public Optional<WithdrawResponse> withdrawFromGameAccount(WithdrawDto request) {
        return Optional.of(generateWithdrawResponse());
    }

    @Override
    public Optional<GmWithdrawResponse> proceedWithdrawFromGameAccount(TransactionReferenceDto transactionReferenceDto) {
        return Optional.empty();
    }

    @Override
    public Optional<GmWithdrawResponse> rollbackWithdrawFromGameAccount(TransactionReferenceDto transactionReferenceDto) {
        return Optional.empty();
    }

    private WithdrawResponse generateWithdrawResponse() {
        return WithdrawResponse.builder()
                .balanceAmount(BigDecimal.TEN)
                .errorData(generateSuccessfulAccountErrorData())
                .success(1)
                .timestamp("2020-01-01 12:00:00")
                .transactionId("merchantId")
                .transStatus(1)
                .version("1")
                .build();
    }

    private DepositResponse generateDepositResponse() {
        return DepositResponse.builder()
                .balanceAmount(BigDecimal.TEN)
                .errorData(generateSuccessfulAccountErrorData())
                .redirectForm("Home")
                .redirectURL("www.home.com")
                .success(1)
                .timestamp("2020-01-01 12:00:00")
                .transactionId("merchantId")
                .transStatus(1)
                .version("1")
                .build();
    }

    @Override
    public Optional<InitiateResponse> initWithdraw(InitiateRequest request) {
        return Optional.of(generateResponse());
    }


}
