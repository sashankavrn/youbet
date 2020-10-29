package com.youbetcha.service;

import com.google.gson.Gson;
import com.youbetcha.exceptions.TransactionNotFoundException;
import com.youbetcha.model.dto.DepositDto;
import com.youbetcha.model.payments.Transaction;
import com.youbetcha.model.payments.TransactionStage;
import com.youbetcha.model.payments.dto.StatusChangeDto;
import com.youbetcha.model.payments.response.InitiateResponse;
import com.youbetcha.model.payments.response.StatusChangeResponse;
import com.youbetcha.model.payments.response.StatusCheckResponse;
import com.youbetcha.model.response.DepositResponse;
import com.youbetcha.model.response.GmWithdrawResponse;
import com.youbetcha.model.response.WithdrawResponse;
import com.youbetcha.repository.TransactionRepository;
import com.youbetcha.util.TransactionStatus;
import com.youbetcha.util.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

    private static final int RETRY_TIME_RANDOM_CEILING = 6;
    
    private static final int MAX_RETRY_TIMES = 3;

	private static final String TX_WITH_MERCHANT_REFERENCE_NOT_FOUND = "Transaction with merchant reference %s not found.";
	
    @Autowired
    private TransactionRepository repository;

    public List<Transaction> getAllByMerchantReference(String merchantReference) {
        return Optional.ofNullable(repository.findAllByMerchantReference(merchantReference))
                .orElseThrow(() -> new TransactionNotFoundException(String.format(
                        TX_WITH_MERCHANT_REFERENCE_NOT_FOUND, merchantReference)));
    }

    public Transaction getByMerchantReferenceAndLatestDate(String merchantReference) {
        return repository.findByMerchantReferenceAndLatestDate(merchantReference)
                .orElseThrow(() -> new TransactionNotFoundException(String.format(
                        TX_WITH_MERCHANT_REFERENCE_NOT_FOUND, merchantReference)));
    }

    public Transaction findByMerchantReferenceTransactionStageAndLatestDate(String merchantReference, TransactionStage ts) {
        return repository.findByMerchantReferenceTransactionStageAndLatestDate(merchantReference, ts.name())
                .orElseThrow(() -> new TransactionNotFoundException(String.format(
                        TX_WITH_MERCHANT_REFERENCE_NOT_FOUND, merchantReference)));
    }

    public Transaction getByMerchantReferenceTransactionStage(String merchantReference, TransactionStage ts) {
        return repository.findByMerchantReferenceAndTransactionStage(merchantReference, ts)
                .orElseThrow(() -> new TransactionNotFoundException(String.format(
                        TX_WITH_MERCHANT_REFERENCE_NOT_FOUND, merchantReference)));
    }

    public List<Transaction> getAllByTransactionStage(TransactionStage ts) {
        return repository.findAllByTransactionStage(ts);
    }

    public void createDepositInitialRequestTX(String merchantReference, String signature, BigDecimal amount, Long userId, String ipAddress, String bonusCode) {
        Transaction tx = Transaction.builder()
                .merchantReference(merchantReference)
                .signature(signature)
                .transactionType(TransactionType.DEPOSIT)
                .transactionStage(TransactionStage.DEPOSIT_INITIAL_REQUEST)
                .status(TransactionStatus.PENDING)
                .amount(amount)
                .playerId(userId)
                .ip(ipAddress)
                .code(bonusCode)
                .build();
        create(tx);
    }

    public void createCashierRequestTx(TransactionStage transactionStage, TransactionType type, String merchantReference, InitiateResponse resp, BigDecimal amount, Long userId, String ipAddress) {
        Transaction tx = Transaction.builder()
                .merchantReference(merchantReference)
                .transactionStage(transactionStage)
                .transactionType(type)
                .status(resp.getResponseCode() == 1 ? TransactionStatus.MM_LOADED : TransactionStatus.MM_NOT_LOADED)
                .transactionCode(resp.getTransactionCode())
                .providerResponse(new Gson().toJson(resp))
                .amount(amount)
                .playerId(userId)
                .ip(ipAddress)
                .build();
        create(tx);
    }

    public void createDepositLimitRequestTx(String merchantReference, TransactionStatus status) {
        Transaction tx = Transaction.builder()
                .merchantReference(merchantReference)
                .transactionType(TransactionType.DEPOSIT)
                .transactionStage(TransactionStage.DEPOSIT_LIMIT_REQUEST)
                .status(status)
                .build();
        create(tx);
    }

    public void createDepositStatusCheckTx(String merchantReference, StatusCheckResponse response) {
        Transaction tx = Transaction.builder()
                .merchantReference(merchantReference)
                .transactionType(TransactionType.DEPOSIT)
                .transactionStage(TransactionStage.DEPOSIT_STATUS_CHECK)
                .status(TransactionStatus.MM_STATUS_CHECK)
                .signature(response.getSignature())
                .transactionCode(response.getTransactionCode())
                .providerResponse(new Gson().toJson(response))
                .build();
        create(tx);
    }

    public void createDepositStatusChangeTx(StatusChangeDto request, StatusChangeResponse response, TransactionStatus ts) {
        Transaction tx = Transaction.builder()
                .merchantReference(request.getMerchantReference())
                .signature(response.getSignature())
                .transactionType(TransactionType.DEPOSIT)
                .status(ts)
                .transactionStage(TransactionStage.DEPOSIT_STATUS_CHANGE)
                .transactionCode(request.getTransactionCode())
//                .playerId(userId)
//                .ip(transaction.getIp())
                .providerResponse(new Gson().toJson(request))
                .build();
        create(tx);
    }

    public void createDepositGMInitialRequestTx(String merchantReference, Long userId, DepositDto depositDTO) {
        Transaction tx = Transaction.builder()
                .merchantReference(merchantReference)
                .transactionType(TransactionType.DEPOSIT)
                .status(TransactionStatus.PENDING)
                .transactionStage(TransactionStage.DEPOSIT_GM_INITIAL_REQUEST)
                .playerId(userId)
                .providerResponse(new Gson().toJson(depositDTO))
                .build();
        create(tx);
    }

    // In the case where the DepositResponse is null/empty
    public void createDepositGMRequestTx(String merchantReference, Long userId) {
        DepositResponse response = new DepositResponse();
        response.setTransStatus(99);
        response.setBalanceAmount(BigDecimal.ZERO);
        createDepositGMRequestTx(merchantReference, userId, response);
    }

    public void createDepositGMRequestTx(String merchantReference, Long userId, DepositResponse depositResponse) {
        Transaction tx = Transaction.builder()
                .merchantReference(merchantReference)
                .transactionType(TransactionType.DEPOSIT)
                .status(depositResponse.getTransStatus() == 1 ? TransactionStatus.GM_SUCCESS : TransactionStatus.GM_FAILURE)
                .transactionStage(TransactionStage.DEPOSIT_GM_REQUEST)
                .amount(depositResponse.getBalanceAmount())
                .playerId(userId)
                .providerResponse(new Gson().toJson(depositResponse))
                .build();
        create(tx);
    }

    public void createDepositGMResponseTx(String merchantReference, Long userId, TransactionStatus ts, Optional<DepositResponse> depositResponse) {
        DepositResponse response = new DepositResponse();
        if (depositResponse.isPresent()) {
            response = depositResponse.get();
        } else {
            response.setBalanceAmount(BigDecimal.ZERO);
        }
        Transaction tx = Transaction.builder()
                .merchantReference(merchantReference)
                .status(ts)
                .transactionType(TransactionType.DEPOSIT)
                .transactionStage(TransactionStage.DEPOSIT_GM_RESPONSE)
                .amount(response.getBalanceAmount())
                .playerId(userId)
                .providerResponse(new Gson().toJson(depositResponse))
                .build();

    	Transaction previousTx = findByMerchantReferenceTransactionStageAndLatestDate(merchantReference, TransactionStage.DEPOSIT_GM_RESPONSE);
        if (previousTx != null) {
        	tx.setRetryCount(previousTx.getRetryCount() + 1);
        	// If our retry count is above our max, instantly fail this transaction for good
        	if (tx.getRetryCount() > MAX_RETRY_TIMES) {
        		tx.setStatus(TransactionStatus.GM_RETRIES_FAILED);
        	}
        } else {
        	tx.setRetryCount(1);
        }
        
        Random r = new Random();
        // Randomly generate a number up to our ceiling - this will be used as minutes
        int random = r.nextInt(RETRY_TIME_RANDOM_CEILING);
        // Multiply by the retry count to increase space between retries
        int retryMinutes = random * tx.getRetryCount();
        // Now convert to a timestamp - ie, add the minutes generated to now()
        LocalDateTime retryTime = LocalDateTime.now().plusMinutes(retryMinutes);
        tx.setRetryTime(retryTime);
        
        create(tx);
    }

    public void create(Transaction transaction) {
        LOGGER.info("Persisting transaction to db with merchant reference: {}, userId: {} and stage: {}", transaction.getMerchantReference(), transaction.getId(), transaction.getTransactionStage().getValue());
        repository.save(transaction);
    }

    public void createWithdrawInitialRequestTX(String merchantReference, String signature, BigDecimal amount, Long userId, String ipAddress) {
        Transaction tx = Transaction.builder()
                .merchantReference(merchantReference)
                .signature(signature)
                .transactionType(TransactionType.WITHDRAW)
                .transactionStage(TransactionStage.WITHDRAW_INITIAL_REQUEST)
                .status(TransactionStatus.PENDING)
                .amount(amount)
                .playerId(userId)
                .ip(ipAddress)
                .build();
        create(tx);
    }

    public void createWithdrawLockRequestTX(String merchantReference, String signature, TransactionStatus ts, WithdrawResponse response, long userId) {
        Transaction tx = Transaction.builder()
                .merchantReference(merchantReference)
                .signature(signature)
                .transactionType(TransactionType.WITHDRAW)
                .status(ts)
                .requestId(response.getTransactionId())
                .transactionStage(TransactionStage.WITHDRAW_GM_REQUEST)
                .playerId(userId)
                .providerResponse(new Gson().toJson(response))
                .build();
        create(tx);
    }

    public void createWithdrawStatusCheckTx(String merchantReference, StatusCheckResponse response) {
        Transaction tx = Transaction.builder()
                .merchantReference(merchantReference)
                .transactionType(TransactionType.WITHDRAW)
                .transactionStage(TransactionStage.WITHDRAW_STATUS_CHECK)
                .status(TransactionStatus.MM_STATUS_CHECK)
                .signature(response.getSignature())
                .transactionCode(response.getTransactionCode())
                .providerResponse(new Gson().toJson(response))
                .build();
        create(tx);
    }

    public void createWithdrawProceedRequestTx(String merchantReference, Long id) {
        Transaction tx = Transaction.builder()
                .merchantReference(merchantReference)
                .transactionType(TransactionType.WITHDRAW)
                .status(TransactionStatus.PENDING)
                .transactionStage(TransactionStage.WITHDRAW_GM_PROCEED_REQUEST)
                .playerId(id)
                .build();
        create(tx);
    }

    public void createWithdrawProceedResponseTx(String merchantReference, TransactionStatus ts,
                                                GmWithdrawResponse response, long userId, BigDecimal amount) {
        Transaction tx = Transaction.builder()
                .merchantReference(merchantReference)
                .status(ts)
                .transactionType(TransactionType.WITHDRAW)
                .transactionStage(TransactionStage.WITHDRAW_GM_PROCEED_RESPONSE)
                .amount(amount)
                .playerId(userId)
                .providerResponse(new Gson().toJson(response))
                .build();
        create(tx);
    }

    public void createWithdrawRollbackRequestTx(String merchantReference, Long id) {
        Transaction tx = Transaction.builder()
                .merchantReference(merchantReference)
                .transactionType(TransactionType.WITHDRAW)
                .status(TransactionStatus.PENDING)
                .transactionStage(TransactionStage.WITHDRAW_GM_ROLLBACK_REQUEST)
                .playerId(id)
                .build();
        create(tx);
    }

    public void createWithdrawRollbackResponseTx(String merchantReference, TransactionStatus ts,
                                                 GmWithdrawResponse response, Long id, BigDecimal confirmedAmount) {
        Transaction tx = Transaction.builder()
                .merchantReference(merchantReference)
                .status(ts)
                .transactionType(TransactionType.WITHDRAW)
                .transactionStage(TransactionStage.WITHDRAW_GM_ROLLBACK_RESPONSE)
                .amount(confirmedAmount)
                .playerId(id)
                .providerResponse(new Gson().toJson(response))
                .build();
        create(tx);
    }

    public void createWithdrawStatusChangeTx(StatusChangeDto request, StatusChangeResponse response, TransactionStatus ts) {
        Transaction tx = Transaction.builder()
                .merchantReference(request.getMerchantReference())
                .signature(response.getSignature())
                .status(ts)
                .transactionType(TransactionType.WITHDRAW)
                .transactionStage(TransactionStage.WITHDRAW_STATUS_CHANGE)
                .transactionCode(request.getTransactionCode())
                .providerResponse(new Gson().toJson(request))
                .build();
        create(tx);
    }
}
