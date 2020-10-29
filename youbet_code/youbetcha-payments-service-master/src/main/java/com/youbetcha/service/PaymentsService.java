package com.youbetcha.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.youbetcha.client.Payment;
import com.youbetcha.exceptions.*;
import com.youbetcha.kafka.PaymentsHandler;
import com.youbetcha.model.ErrorData;
import com.youbetcha.model.Player;
import com.youbetcha.model.PlayerAccount;
import com.youbetcha.model.dto.DepositDto;
import com.youbetcha.model.dto.TransactionReferenceDto;
import com.youbetcha.model.dto.WithdrawDto;
import com.youbetcha.model.event.PaymentEvent;
import com.youbetcha.model.mapping.AccountMapping;
import com.youbetcha.model.mapping.PlayerMapping;
import com.youbetcha.model.payments.InitiateRequest;
import com.youbetcha.model.payments.InitiateType;
import com.youbetcha.model.payments.Transaction;
import com.youbetcha.model.payments.TransactionStage;
import com.youbetcha.model.payments.dto.InitiateDto;
import com.youbetcha.model.payments.dto.StatusChangeDto;
import com.youbetcha.model.payments.dto.StatusCheckDto;
import com.youbetcha.model.payments.response.InitiateResponse;
import com.youbetcha.model.payments.response.MaxDepositResponse;
import com.youbetcha.model.payments.response.StatusChangeResponse;
import com.youbetcha.model.payments.response.StatusCheckResponse;
import com.youbetcha.model.response.DepositResponse;
import com.youbetcha.model.response.GmWithdrawResponse;
import com.youbetcha.model.response.WithdrawResponse;
import com.youbetcha.repository.AccountMappingRepository;
import com.youbetcha.repository.PlayerMappingRepository;
import com.youbetcha.service.assembler.Assembler;
import com.youbetcha.util.MMTransactionStatus;
import com.youbetcha.util.TransactionStatus;
import com.youbetcha.util.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class PaymentsService {

    private static final String WELCOME_BONUS_CODE = "WELCOMEBONUS";
    public static final String MM_STATUS_CHANGE_ACTION_DEPOSIT = "DEPOSIT";
    public static final String MM_STATUS_CHANGE_ACTION_WITHDRAW = "WITHDRAW";
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentsService.class);

    Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy, HH:mm:ss").create();

    @Autowired
    PaymentsHandler paymentsHandler;
    @Autowired
    private SignatureService signatureService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private GMConnectionService gmConnectionService;
    @Autowired
    private PlayerMappingRepository playerRepository;
    @Autowired
    private AccountMappingRepository accountsRepository;
    @Autowired
    private Payment paymentClient;
    @Autowired
    private Assembler assembler;

    public CompletableFuture<InitiateResponse> initRequest(String sessionId, InitiateDto initiateDto) {
        String merchantReference = UUID.randomUUID().toString();
        transactionService.getAllByMerchantReference(merchantReference).stream()
                .findAny().ifPresent(x -> {
            throw new DuplicateMerchantReferenceFoundException(merchantReference + " is duplicated");
        });
        PaymentEvent paymentEvent = PaymentEvent.builder()
                .merchantReference(merchantReference)
                .sessionId(sessionId)
                .build();
        paymentsHandler.fetchUserDetails(paymentEvent);
        int tryCount = 0;
        int maxLimit = 20;
        while (!playerRepository.findByKeyValue(merchantReference).stream().findFirst().isPresent() &&
                !accountsRepository.findByKeyValue(merchantReference).stream().findFirst().isPresent() &&
                accountsRepository.findByKeyValue(merchantReference).stream()
                        .allMatch(x -> gson.fromJson(x.getPayload(), PlayerAccount.class).getMaxDepositResponse() == null)) {
            if (tryCount == maxLimit) {
                throw new MaxRetriesExceededException(String.format("Player service was unreacheable after %s retries", maxLimit));
            }
            try {
                LOGGER.info("Waiting for response...");
                Thread.sleep(3000);
                tryCount++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }

        PlayerMapping playerMapping = playerRepository.findByKeyValue(merchantReference).stream().findFirst().orElseThrow(
                () -> new PlayerNotFoundException(String.format("Player with merchant reference: %s not found", merchantReference)));
        Player player = gson.fromJson(playerMapping.getPayload(), Player.class);
        AccountMapping accountMapping = accountsRepository.findByKeyValue(merchantReference).stream()
                .filter(x -> gson.fromJson(x.getPayload(), PlayerAccount.class).getMaxDepositResponse() != null)
                .findFirst().orElseThrow(
                () -> new AccountNotFoundException(String.format("Account with merchant reference: %s not found", merchantReference)));
        PlayerAccount playerAccount = gson.fromJson(accountMapping.getPayload(), PlayerAccount.class);
        InitiateRequest request = assembler.fetchAndAssemble(player, initiateDto, merchantReference);
        request.setSignature(signatureService.signForInitiateRequest(request));

        if (initiateDto.getInitiateType().equals(InitiateType.DEPOSIT)) {
            transactionService.createDepositInitialRequestTX(merchantReference, request.getSignature(),
                    initiateDto.getAmount(), player.getId(), initiateDto.getIpAddress(), initiateDto.getBonusCode());

            MaxDepositResponse maxDepositResponse = playerAccount.getMaxDepositResponse();
            BigDecimal amountLimit = maxDepositResponse.getAmount();
            BigDecimal amount = initiateDto.getAmount();
            if (!amountLimit.equals(BigDecimal.ZERO) || maxDepositResponse.getErrorData().getErrorCode() != 1031) {
                if (amountLimit.compareTo(amount) < 0) {
                    transactionService.createDepositLimitRequestTx(merchantReference, TransactionStatus.LIMIT_NOT_ALLOWED);
                    throw new InsufficientDepositLimitException(String.format("Player with email: [%s] and merchant reference: " +
                            "[%s] deposit request amount is greater than deposit limit", player.getEmail(), merchantReference));
                }
            }
            transactionService.createDepositLimitRequestTx(merchantReference, TransactionStatus.LIMIT_ALLOWED);
        } else {
            transactionService.createWithdrawInitialRequestTX(merchantReference, request.getSignature(),
                    initiateDto.getAmount(), player.getId(), initiateDto.getIpAddress());

            WithdrawDto withdrawDto = WithdrawDto.builder()
                    .userId(String.valueOf(player.getEverymatrixUserId()))
                    .requestAmount(initiateDto.getAmount())
                    .sessionId(sessionId)
                    .debitAccountId(playerAccount.getId())
                    .requestCurrency(player.getCurrency())
                    .transactionReference(merchantReference)
                    .userIp(initiateDto.getIpAddress())
                    .build();
            WithdrawResponse resp = paymentClient.withdrawFromGameAccount(withdrawDto)
                    .orElseThrow(() -> new RuntimeException("No resp from GM"));
            if (resp.getSuccess() == 1) {
                transactionService.createWithdrawLockRequestTX(merchantReference, request.getSignature(), TransactionStatus.GM_SUCCESS, resp, player.getId());
            } else {
                transactionService.createWithdrawLockRequestTX(merchantReference, request.getSignature(), TransactionStatus.GM_FAILURE, resp, player.getId());
            }
        }

        Optional<InitiateResponse> response = initiateDto.getInitiateType() != InitiateType.WITHDRAW ?
                paymentClient.initDeposit(request) :
                paymentClient.initWithdraw(request);
        InitiateResponse resp;
        if (response.isPresent()) {
            resp = response.get();
            if (initiateDto.getInitiateType().equals(InitiateType.WITHDRAW)) {
                transactionService.createCashierRequestTx(TransactionStage.WITHDRAW_CASHIER_REQUEST, TransactionType.WITHDRAW, merchantReference, resp, initiateDto.getAmount(), player.getId(), initiateDto.getIpAddress());
            } else {
                transactionService.createCashierRequestTx(TransactionStage.DEPOSIT_CASHIER_REQUEST, TransactionType.DEPOSIT, merchantReference, resp, initiateDto.getAmount(), player.getId(), initiateDto.getIpAddress());
            }
        }

        InitiateResponse initiateResponse = response.orElseThrow(() -> new InitiateTransactionException(
                "Initiating " + initiateDto.getInitiateType() + " failed"));
        return CompletableFuture.completedFuture(initiateResponse);
    }

    public StatusCheckResponse checkTransactionStatus(StatusCheckDto request) {
    	Transaction transaction = null;
    	StatusCheckResponse response = new StatusCheckResponse();
    	try {
    		transaction = transactionService.getByMerchantReferenceAndLatestDate(request.getMerchantReference());
    		if (transaction.getStatus().equals(TransactionStatus.MM_STATUS_CHECK)) {
    			// There's been status checks already, so let's not compare to that, but instead look for the last relevant Transaction
    			TransactionStage ts = TransactionStage.WITHDRAW_CASHIER_REQUEST;
        		if (transaction.getTransactionType().equals(TransactionType.DEPOSIT)) {
        			ts = TransactionStage.DEPOSIT_CASHIER_REQUEST;
        		}	
        		transaction = transactionService.getByMerchantReferenceTransactionStage(request.getMerchantReference(), ts);
    		}
    		response = assembler.assembleResponse(request, transaction);
    		
    		if (transaction.getTransactionType().equals(TransactionType.DEPOSIT)) {
    			transactionService.createDepositStatusCheckTx(request.getMerchantReference(), response);
    		} else {
    			transactionService.createWithdrawStatusCheckTx(request.getMerchantReference(), response);
    		}
        } catch (TransactionNotFoundException e) {
        	// If they're sending us a missing merchant ref, respond to tell them but in a way that's a 200 OK
        	response.setResponseStatus(1);
        	response.setStatus(2);
        	response.setResponseMessage(e.getMessage());
        	response.setAction(request.getAction());
        	response.setTransactionCode("Transaction Not Found");
        }

        response.setSignature(signatureService.signForStatusCheck(response));
        return response;
    }

    public StatusChangeResponse changeTransactionStatus(StatusChangeDto request) {
        // TODO - DEPOSIT_STATUS_CHANGE - MM_SUCCESS | MM_ERROR | MM_CANCELLED | MM_REJECTED | MM_PENDING

        // First thing is to check if we have an actual transaction on our side matching the provided merchant reference.
        // Then we need to check that this flow hasn't been completed with MM already - look for DEPOSIT_GM_INITIAL_REQUEST
        // Then check the DEPOSIT_STATUS_CHANGE status, and if PENDING then we can continue, otherwise ignore
        // Unless we don't have one, in which case this is the first status change so we'll honour it
        // Now we can finally check the status from MM, and then:
        //     If success, continue with the deposit/withdraw
        //     If not success, create a new transaction with the updated info (including PENDING)

        List<Transaction> transactions = transactionService.getAllByMerchantReference(request.getMerchantReference());
        if (transactions.isEmpty()) {
            LOGGER.warn("W> Status change could not find transactions for reference {}.", request.getMerchantReference());
            return buildStatusChangeResponse(request, MMTransactionStatus.ERROR, "Merchant reference not found.");
        }
        Transaction gmRequest;
        if (request.getAction().equals(MM_STATUS_CHANGE_ACTION_WITHDRAW)) {
            gmRequest = extractByTransactionType(transactions, TransactionStage.WITHDRAW_GM_INITIAL_REQUEST);
        } else {
            gmRequest = extractByTransactionType(transactions, TransactionStage.DEPOSIT_GM_INITIAL_REQUEST);
        }

        if (gmRequest != null) {
            LOGGER.warn("W> Status change from MM attempted to update a completed tx with reference {}.", request.getMerchantReference());
            return buildStatusChangeResponse(request, MMTransactionStatus.SUCCESS, "Transaction already completed with MoneyMatrix.");
        }
        Transaction mmRequest;
        if (request.getAction().equals(MM_STATUS_CHANGE_ACTION_WITHDRAW)) {
            mmRequest = extractByTransactionType(transactions, TransactionStage.WITHDRAW_STATUS_CHANGE);
        } else {
            mmRequest = extractByTransactionType(transactions, TransactionStage.DEPOSIT_STATUS_CHANGE);
        }
        if (mmRequest != null && !mmRequest.getStatus().equals(TransactionStatus.MM_PENDING)) {
            LOGGER.warn("W> Status change from MM attempted to update a failed tx with reference {} and status {}.", request.getMerchantReference(), mmRequest.getStatus());
            return buildStatusChangeResponse(request, MMTransactionStatus.SUCCESS, "Transaction already failed with MoneyMatrix.");
        }

        // Now we should have either a pending transaction or no previous transaction

        StatusChangeResponse response = null;
        LOGGER.info("I> Status update from MM: MerchantRef {} now has status of {}.", request.getMerchantReference(), request.getStatus());
        // Authorised is success but for credit cards

        if (request.getAction().equals(MM_STATUS_CHANGE_ACTION_WITHDRAW)) {
            if ((request.getStatus() == MMTransactionStatus.SUCCESS.getValue() || request.getStatus() == MMTransactionStatus.AUTHORIZED.getValue())
                    && request.getAction().equalsIgnoreCase(MM_STATUS_CHANGE_ACTION_WITHDRAW)) {
                response = buildStatusChangeResponse(request, MMTransactionStatus.SUCCESS, "Transaction success acknowledged.");
                // Create a tx that this payment was successfully processed, now we can continue and inform GM
                transactionService.createWithdrawStatusChangeTx(request, response, TransactionStatus.MM_SUCCESS);
                proceedWithdrawFromGameAccount(request);
            } else if (request.getStatus() != MMTransactionStatus.SUCCESS.getValue() && request.getStatus() != MMTransactionStatus.AUTHORIZED.getValue()
                    && request.getAction().equalsIgnoreCase(MM_STATUS_CHANGE_ACTION_WITHDRAW)) {
                // TODO - is SUCCESS correct here?
                response = buildStatusChangeResponse(request, MMTransactionStatus.SUCCESS, "Transaction update acknowledged.");
                TransactionStatus ts = TransactionStatus.MM_PENDING;
                if (request.getStatus() == MMTransactionStatus.ERROR.getValue()) {
                    ts = TransactionStatus.MM_ERROR;
                } else if (request.getStatus() == MMTransactionStatus.CANCELLED.getValue() || request.getStatus() == MMTransactionStatus.VOIDED.getValue()) {
                    ts = TransactionStatus.MM_CANCELLED;
                } else if (request.getStatus() == MMTransactionStatus.REJECTED.getValue()) {
                    ts = TransactionStatus.MM_REJECTED;
                }
                // Create a tx reflecting what MM have updated us with such as pending, or a failure
                transactionService.createWithdrawStatusChangeTx(request, response, ts);
            }
        } else {
            if ((request.getStatus() == MMTransactionStatus.SUCCESS.getValue() || request.getStatus() == MMTransactionStatus.AUTHORIZED.getValue())
                    && request.getAction().equalsIgnoreCase(MM_STATUS_CHANGE_ACTION_DEPOSIT)) {
                response = buildStatusChangeResponse(request, MMTransactionStatus.SUCCESS, "Transaction success acknowledged.");
                // Create a tx that this payment was successfully processed, now we can continue and inform GM
                transactionService.createDepositStatusChangeTx(request, response, TransactionStatus.MM_SUCCESS);
                depositToGameAccount(request);
            } else if (request.getStatus() != MMTransactionStatus.SUCCESS.getValue() && request.getStatus() != MMTransactionStatus.AUTHORIZED.getValue()
                    && request.getAction().equalsIgnoreCase(MM_STATUS_CHANGE_ACTION_DEPOSIT)) {
                // TODO - is SUCCESS correct here?
                response = buildStatusChangeResponse(request, MMTransactionStatus.SUCCESS, "Transaction update acknowledged.");
                TransactionStatus ts = TransactionStatus.MM_PENDING;
                if (request.getStatus() == MMTransactionStatus.ERROR.getValue()) {
                    ts = TransactionStatus.MM_ERROR;
                } else if (request.getStatus() == MMTransactionStatus.CANCELLED.getValue() || request.getStatus() == MMTransactionStatus.VOIDED.getValue()) {
                    ts = TransactionStatus.MM_CANCELLED;
                } else if (request.getStatus() == MMTransactionStatus.REJECTED.getValue()) {
                    ts = TransactionStatus.MM_REJECTED;
                }
                // Create a tx reflecting what MM have updated us with such as pending, or a failure
                transactionService.createDepositStatusChangeTx(request, response, ts);
            }
        }
        return response;
    }

    private Transaction extractByTransactionType(List<Transaction> transactions, TransactionStage transactionStage) {
        transactions.sort((Transaction t1, Transaction t2) -> t2.getCreatedDate().compareTo(t1.getCreatedDate()));

        return transactions.stream().filter(tx -> tx.getTransactionStage().equals(transactionStage))
                .findFirst().orElse(null);
    }

    private StatusChangeResponse buildStatusChangeResponse(StatusChangeDto request, MMTransactionStatus status, String message) {
        StatusChangeResponse response = StatusChangeResponse.builder()
                .action(request.getAction())
                .responseStatus(status.getValue())
                .responseMessage(message)
                .build();

        response.setSignature(signatureService.signForStatusChange(response));
        return response;
    }

    @Async
    public CompletableFuture<GmWithdrawResponse> proceedWithdrawFromGameAccount(StatusChangeDto dto) {
        PlayerMapping playerMapping = playerRepository.findByKeyValue(String.valueOf(dto.getMerchantReference()))
                .stream().findFirst().orElseThrow(() -> new PlayerNotFoundException(String.format("Player with merchant reference: %s not found", dto.getMerchantReference())));
        Player player = gson.fromJson(playerMapping.getPayload(), Player.class);
        TransactionReferenceDto transactionReferenceDto =
                TransactionReferenceDto.builder().transactionReference(dto.getMerchantReference()).build();
        transactionService.createWithdrawProceedRequestTx(dto.getMerchantReference(), player.getId());

        Optional<GmWithdrawResponse> withdrawResponse = paymentClient.proceedWithdrawFromGameAccount(transactionReferenceDto);
        GmWithdrawResponse response = null;
        if (withdrawResponse.isPresent()) {
            response = withdrawResponse.get();
        }
        // We've logged the response from GM, now to see if we need to retry, which is based on the transStatus != 1
        if (response == null || response.getTransStatus() != 1) {
            if (response != null) {
                transactionService.createWithdrawProceedResponseTx(dto.getMerchantReference(), TransactionStatus.GM_FAILURE, response, player.getId(), dto.getConfirmedAmount());
            }
            transactionService.createWithdrawRollbackRequestTx(dto.getMerchantReference(), player.getId());
            Optional<GmWithdrawResponse> rollbackWithdrawResp = paymentClient
                    .rollbackWithdrawFromGameAccount(transactionReferenceDto);

            if (rollbackWithdrawResp.isPresent()) {
                GmWithdrawResponse resp = rollbackWithdrawResp.get();
                if (resp.getTransStatus() == 1) {
                    transactionService.createWithdrawRollbackResponseTx(dto.getMerchantReference(),
                            TransactionStatus.GM_SUCCESS, resp, player.getId(), dto.getConfirmedAmount());
                    //retry
                } else {
                    transactionService.createWithdrawRollbackResponseTx(dto.getMerchantReference(),
                            TransactionStatus.GM_FAILURE, resp, player.getId(), dto.getConfirmedAmount());
                }
            }
        } else {
            transactionService.createWithdrawProceedResponseTx(dto.getMerchantReference(), TransactionStatus.GM_SUCCESS,
                    response, player.getId(), dto.getConfirmedAmount());
        }

        // If the ProceedWithdrawResponse is still null, then it means the initial attempt failed
        // We should create a response with reasonable values to return back to the user
        if (response == null) {
            ErrorData ed = ErrorData.builder()
                    .errorMessage("Error communicating with GamMatrix Server.")
                    .errorCode(Short.valueOf("99"))
                    .build();
            response = GmWithdrawResponse.builder()
                    .errorData(ed)
                    .success(0)
                    .transStatus(2)
                    .build();
        }

        return CompletableFuture.completedFuture(response);
    }

    @Async
    public CompletableFuture<DepositResponse> depositToGameAccount(StatusChangeDto dto) {
        PlayerMapping playerMapping = playerRepository.findByKeyValue(String.valueOf(dto.getMerchantReference()))
                .stream().findFirst().orElseThrow(() -> new PlayerNotFoundException(String.format("Player with merchant reference: %s not found", dto.getMerchantReference())));
        Player player = gson.fromJson(playerMapping.getPayload(), Player.class);
        AccountMapping accountMapping = accountsRepository.findByKeyValue(String.valueOf(dto.getMerchantReference()))
                .stream().findFirst().orElseThrow(() -> new AccountNotFoundException(String.format("Account with merchant reference: %s not found", dto.getMerchantReference())));
        PlayerAccount playerAccount = gson.fromJson(accountMapping.getPayload(), PlayerAccount.class);

        DepositDto build = DepositDto.builder()
                .userId(String.valueOf(player.getEverymatrixUserId()))
                .creditAccountId(playerAccount.getId())
                .requestAmount(dto.getAmount())
                .requestCurrency(player.getCurrency())
                .transactionReference(dto.getMerchantReference())
                .userIp(player.getSignupIp())
                .build();
        // It's their first deposit and they accepted the welcome bonus at registration - apply it here
        if (player.isBonusOptIn() && player.getDepositCount() < 1) {
            build.setApplyBonusCode(WELCOME_BONUS_CODE);
        } else if (player.getDepositCount() >= 1) {
        	// This isn't their first deposit, so check if they have a bonus code we should be applying
        	Transaction initialTx = transactionService.getByMerchantReferenceTransactionStage(dto.getMerchantReference(), TransactionStage.DEPOSIT_INITIAL_REQUEST);
        	if (initialTx.getCode() != null && !initialTx.getCode().isEmpty()) {
        		build.setApplyBonusCode(initialTx.getCode().toUpperCase());
        	}
        }
        transactionService.createDepositGMInitialRequestTx(dto.getMerchantReference(), player.getId(), build);

        // Execute this call to GM immediately
        LOGGER.debug("D> Calling initial GM Deposit with payload {}", build);
        Optional<DepositResponse> depositResponse = gmConnectionService.executeDepositJob(build);
        DepositResponse response = depositResponse.orElse(null);
        LOGGER.debug("D> Initial GM Deposit call returned: {}", response);

        // If the DepositResponse is still null, then it means the initial attempt failed
        // We should create a response with reasonable values to return back to the user
        if (response == null) {
            ErrorData ed = ErrorData.builder()
                    .errorMessage("Error communicating with GamMatrix Server.")
                    .errorCode(Short.valueOf("99"))
                    .build();
            response = DepositResponse.builder()
                    .balanceAmount(dto.getAmount())
                    .errorData(ed)
                    .success(0)
                    .transStatus(2)
                    .build();
        }

        return CompletableFuture.completedFuture(response);
    }

}
