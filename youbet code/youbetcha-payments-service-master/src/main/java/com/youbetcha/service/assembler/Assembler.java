package com.youbetcha.service.assembler;

import com.youbetcha.model.Player;
import com.youbetcha.model.payments.InitiateRequest;
import com.youbetcha.model.payments.Transaction;
import com.youbetcha.model.payments.TransactionStage;
import com.youbetcha.model.payments.dto.InitiateDto;
import com.youbetcha.model.payments.dto.StatusCheckDto;
import com.youbetcha.model.payments.response.InitiateResponse;
import com.youbetcha.model.payments.response.StatusCheckResponse;
import com.youbetcha.util.TransactionStatus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class Assembler {

    @Value("${everymatrix.money-matrix.callback-url}")
    private String callbackUrl;

    @Value("${everymatrix.money-matrix.check-status-url}")
    private String checkStatusUrl;

    public InitiateRequest fetchAndAssemble(Player player, InitiateDto dto, String merchantReference) {
        return InitiateRequest.builder()
                .address(player.getAddress1())
                .birthDate(formatDateStringToStringMMddyyyy(player.getBirthDate()))
                .city(player.getCity())
                .countryCode(player.getCountryCode())
                .customerId(String.valueOf(player.getId()))
                .currency(player.getCurrency())
                .registrationDate(formatDateToStringMMddyyyy(player.getCreatedDate()))
                .registrationIpAddress(player.getSignupIp())
                .emailAddress(player.getEmail())
                .postalCode(player.getPostalCode())
                .firstName(player.getFirstName())
                .lastName(player.getLastName())
                .language(player.getLanguage())
                .phoneNumber(player.getMobile())
                .amount(dto.getAmount().setScale(2))
                .ipAddress(dto.getIpAddress())
                .merchantReference(merchantReference)
                .callbackUrl(callbackUrl)
                .checkStatusUrl(checkStatusUrl)
                .build();
    }

    private String formatDateStringToStringMMddyyyy(String birthDate) {
        LocalDate parse = LocalDate.parse(birthDate);
        return formatDateToStringMMddyyyy(Date.from(parse.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
    }

    public Transaction assembleTransaction(InitiateRequest request, InitiateResponse response, InitiateDto dto, Player player) {
        return Transaction.builder()
                .merchantReference(request.getMerchantReference())
                .signature(request.getSignature())
                .status(TransactionStatus.PENDING)
                .transactionStage(TransactionStage.valueOf(dto.getInitiateType().getValue()))
                .transactionCode(response.getTransactionCode())
                .requestId(response.getRequestId())
                .playerId(player.getId())
                .ip(dto.getIpAddress())
                .build();
    }

    private String formatDateToStringMMddyyyy(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(date);
    }

    public StatusCheckResponse assembleResponse(StatusCheckDto request, Transaction transaction) {
    	String transactionCode = "Transaction Not Found";
    	String message = "Transaction Not Found";
    	int responseStatus = 2;
    	int status = 2;
    	if (transaction != null) {
    		transactionCode = transaction.getTransactionCode();
    		message = "OK";
    		// FIXME - figure out if this is sufficient for MM
    		responseStatus = 1;
    		status = mapStatusToValue(transaction.getStatus());
    	}
    	
        return StatusCheckResponse.builder()
                .action(request.getAction())
                .transactionCode(transactionCode)
                .responseMessage(message)
                .responseStatus(responseStatus)
                .status(status)
                .build();
        
    }

	private int mapStatusToValue(TransactionStatus status) {
		switch (status) {
			case MM_SUCCESS:
				return 1; // Success
			case MM_LOADED:
				return 1; // Success
			case MM_ERROR:
				return 2; // Error
			case MM_FAILURE:
				return 2; // Error
			case MM_CANCEL:
				return 6; // Cancelled
			case MM_PENDING:
				return 3; // PendingNotification
			case MM_CANCELLED:
				return 6; // Cancelled
			case MM_REJECTED:
				return 5; // Rejected
			default:
				return 2; // Default to error
		}
	}
}
