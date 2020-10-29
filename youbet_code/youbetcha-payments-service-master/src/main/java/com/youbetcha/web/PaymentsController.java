package com.youbetcha.web;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.youbetcha.exceptions.InitiateTransactionException;
import com.youbetcha.exceptions.TransactionNotFoundException;
import com.youbetcha.model.payments.dto.InitiateDto;
import com.youbetcha.model.payments.dto.StatusChangeDto;
import com.youbetcha.model.payments.dto.StatusCheckDto;
import com.youbetcha.model.payments.response.InitiateResponse;
import com.youbetcha.model.payments.response.StatusChangeResponse;
import com.youbetcha.model.payments.response.StatusCheckResponse;
import com.youbetcha.model.response.DepositResponse;
import com.youbetcha.service.PaymentsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Payments")
public class PaymentsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentsController.class);

    private static Function<Throwable, ResponseEntity<DepositResponse>> handleDepositFailure = throwable -> {
        LOGGER.error("Failed to deposit funds: %s", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };
    private static Function<Throwable, ResponseEntity<InitiateResponse>> handleInitFailure = throwable -> {
        LOGGER.error("Failed to Initialize Transaction: %s", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };
    @Autowired
    PaymentsService service;

    @PostMapping("/api/v1/payments/init/{sessionId}")
    @ApiOperation(value = "Initiate Hosted Cashier")
    public String initiateCashTransaction(
            @RequestBody InitiateDto request,
            @RequestHeader(name = "x-client-ip") String clientIp,
            @PathVariable String sessionId) {
        request.setIpAddress(clientIp);
        LOGGER.info("Attempting to initiate {} request", request.getInitiateType());
        CompletableFuture<ResponseEntity<InitiateResponse>> future = service.initRequest(sessionId, request).thenApply(ResponseEntity::ok).exceptionally(handleInitFailure);
        try {
			ResponseEntity<InitiateResponse> responseEntity = future.get();
			if (responseEntity != null && responseEntity.getBody().getResponseCode() == 1) {
				return responseEntity.getBody().getCashierUrl();
			}
		} catch (InterruptedException e) {
			LOGGER.error("E> InterruptedExeption getting HC for sessionId {}.", sessionId);
			e.printStackTrace();
		} catch (ExecutionException e) {
			LOGGER.error("E> ExecutionException getting HC for sessionId {}.", sessionId);
			e.printStackTrace();
		}
        return null;
    }

    @PostMapping("/external-api/v1/payments/transaction/status/check")
    @ApiOperation(value = "Check transaction status")
    public ResponseEntity<StatusCheckResponse> checkTransactionStatus(
            @RequestBody StatusCheckDto request) {
        LOGGER.info("Attempting to check transaction status: {}", request);
        StatusCheckResponse response = service.checkTransactionStatus(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/external-api/v1/payments/transaction/status/change")
    @ApiOperation(value = "Change transaction status")
    public ResponseEntity<StatusChangeResponse> changeTransactionStatus(
            @RequestBody StatusChangeDto request) {
        LOGGER.info("Attempting to change transaction status: {}", request);
        StatusChangeResponse response = service.changeTransactionStatus(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // Success
    // TODO - figure out if we need the optional endpoints for MM to call after a change in state
    // Fail
    
    // Cancel
    
    

    /*@PostMapping("/deposit")
    @ApiOperation(value = "***FOR TESTING PURPOSES ONLY*** Deposit to game account")
    @Profile(value = "!prod")
    public CompletableFuture<ResponseEntity<DepositResponse>> depositToGameAccount(@RequestBody StatusChangeDto request) {
        LOGGER.info("Attempting to deposit to game account: {}", request);
        return service.depositToGameAccount(request).thenApply(ResponseEntity::ok)
                .exceptionally(handleDepositFailure);
    }*/

    @ExceptionHandler({InitiateTransactionException.class})
    public ResponseEntity<Object> handleException(InitiateTransactionException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({TransactionNotFoundException.class})
    public ResponseEntity<Object> handleException(TransactionNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
