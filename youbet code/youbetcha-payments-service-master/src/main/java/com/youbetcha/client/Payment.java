package com.youbetcha.client;

import com.youbetcha.model.dto.DepositDto;
import com.youbetcha.model.dto.TransactionReferenceDto;
import com.youbetcha.model.dto.WithdrawDto;
import com.youbetcha.model.payments.InitiateRequest;
import com.youbetcha.model.payments.response.InitiateResponse;
import com.youbetcha.model.response.DepositResponse;
import com.youbetcha.model.response.GmWithdrawResponse;
import com.youbetcha.model.response.WithdrawResponse;

import java.util.Optional;

public interface Payment {
    Optional<InitiateResponse> initDeposit(InitiateRequest request);

    Optional<InitiateResponse> initWithdraw(InitiateRequest request);

    Optional<DepositResponse> depositToGameAccount(DepositDto request);

    Optional<WithdrawResponse> withdrawFromGameAccount(WithdrawDto request);

    Optional<GmWithdrawResponse> proceedWithdrawFromGameAccount(TransactionReferenceDto transactionReferenceDto);

    Optional<GmWithdrawResponse> rollbackWithdrawFromGameAccount(TransactionReferenceDto transactionReferenceDto);
}
