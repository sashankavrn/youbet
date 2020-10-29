package com.youbetcha.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.youbetcha.exceptions.PlayerNotFoundException;
import com.youbetcha.kafka.PaymentsHandler;
import com.youbetcha.model.dto.TransactionReferenceDto;
import com.youbetcha.model.event.PlayerEvent;
import com.youbetcha.model.event.PushNotification;
import com.youbetcha.model.mapping.PlayerMapping;
import com.youbetcha.model.payments.TransactionStage;
import com.youbetcha.model.response.GmWithdrawResponse;
import com.youbetcha.repository.PlayerMappingRepository;
import com.youbetcha.util.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.youbetcha.client.Payment;
import com.youbetcha.model.Player;
import com.youbetcha.model.dto.DepositDto;
import com.youbetcha.model.payments.Transaction;
import com.youbetcha.model.response.DepositResponse;
import com.youbetcha.util.TransactionStatus;

@Service
public class GMConnectionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GMConnectionService.class);

	@Autowired
	private TransactionService transactionService;
	@Autowired
	private PlayerMappingRepository playerRepository;
    @Autowired
    private Payment paymentClient;
	@Autowired
	PaymentsHandler paymentsHandler;


	Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy, HH:mm:ss").create();

	// Run this every 2 minutes
	@Scheduled(fixedRate = 120000)
	public void executeRetryJobs() {
		retryDepositJobs();
		retryWithdrawJobs();
	}

	private void retryWithdrawJobs() {
		LOGGER.debug("D> About to retry Withdraw jobs...");
		executeJob(TransactionStage.WITHDRAW_GM_PROCEED_REQUEST,
				TransactionStage.WITHDRAW_GM_PROCEED_RESPONSE, TransactionReferenceDto.class);
	}

	private <T> void executeJob(TransactionStage requestTxStage, TransactionStage responseTxStage, Class<T> clazz) {
		List<Transaction> transactions = transactionService
				.getAllByTransactionStage(requestTxStage);

		// Now we need to break down our transactions based on the merchantReference
		Map<String, List<Transaction>> separatedTransactions = new HashMap<>();
		for (Transaction t : transactions) {
			if (separatedTransactions.containsKey(t.getMerchantReference())) {
				List<Transaction> list = separatedTransactions.get(t.getMerchantReference());
				list.add(t);
				list.sort((Transaction t1, Transaction t2) -> t2.getCreatedDate().compareTo(t1.getCreatedDate()));
				separatedTransactions.put(t.getMerchantReference(), list);
			} else {
				List<Transaction> newList = new ArrayList<>();
				newList.add(t);
				separatedTransactions.put(t.getMerchantReference(), newList);
			}
		}
		LOGGER.debug("D> Found {} transactions with {} unique keys.", transactions.size(), separatedTransactions.keySet().size());

		// With our sorted list per key, go through each one and see if they're completed (one way or another)
		for (String key : separatedTransactions.keySet()) {
			List<Transaction> list = separatedTransactions.get(key);
			boolean completed = false;
			for (Transaction t : list) {
				if (t.getStatus().equals(TransactionStatus.GM_RETRIES_FAILED)
						|| t.getStatus().equals(TransactionStatus.GM_FAILED)
						|| t.getStatus().equals(TransactionStatus.GM_PASSED)
						|| t.getStatus().equals(TransactionStatus.GM_SUCCESS)) {
					completed = true;
					LOGGER.debug("D> Transaction key {} has status {} and is complete.", key, t.getStatus());
				}
			}
			if (!completed) {
				LOGGER.debug("D> Calling latest tx with key {} and stage {}", key, responseTxStage.name());
				// Fetch the corresponding TX
				Transaction retryTx = transactionService.findByMerchantReferenceTransactionStageAndLatestDate(key, responseTxStage);
				LOGGER.debug("D> Transaction key {} with status {} will be retried.", key, retryTx.getStatus());
				//	Transaction retryTx = list.stream().filter(tx -> tx.getStatus().equals(TransactionStatus.GM_RETRY)).findFirst().orElse(null);

				if(retryTx.getRetryTime() == null) {
					retryTx.setRetryTime(LocalDateTime.now());
				}
				LOGGER.debug("D> Tx with retry count of {} is scheduled to retry at {} - {}", retryTx.getRetryCount(), retryTx.getRetryTime(), retryTx.getRetryTime().isBefore(LocalDateTime.now()));
				// Check if the timer says we should go now - if now() is after the retryTime we're good to go
				if (retryTx.getRetryTime().isBefore(LocalDateTime.now())) {
					ObjectMapper om = new ObjectMapper();
					T dto = null;
					try {
						dto = om.readValue(retryTx.getProviderResponse(), clazz);
						LOGGER.debug("D> Retrying key {} for withdraw with payload: {}", key, dto);
					} catch (JsonMappingException e) {
						LOGGER.error(String.format("E> JsonMapping exception with response: %s", retryTx.getProviderResponse()));
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						LOGGER.error(String.format("E> JsonProcessing exception with response: %s", retryTx.getProviderResponse()));
						e.printStackTrace();
					}
					if(retryTx.getTransactionType().equals(TransactionType.WITHDRAW)) {
						executeWithdrawJob((TransactionReferenceDto) dto);
					} else {
						executeDepositJob((DepositDto) dto);
					}
				}
			}
		}
	}

	private void retryDepositJobs() {
		LOGGER.debug("D> About to retry Deposit jobs...");
		executeJob(TransactionStage.DEPOSIT_GM_REQUEST, TransactionStage.DEPOSIT_GM_RESPONSE, DepositDto.class);
	}

	public Optional<DepositResponse> executeDepositJob(DepositDto depositDTO) {
		LOGGER.debug("D> Attempting to retry dep to GM for key {} is: {}", depositDTO.getTransactionReference(), depositDTO);
		Optional<DepositResponse> depositResponse = paymentClient.depositToGameAccount(depositDTO);
		LOGGER.debug("D> Deposit response from GM {}", depositResponse);
		PlayerMapping playerMapping = playerRepository.findByKeyValue(String.valueOf(depositDTO.getTransactionReference()))
				.stream().findFirst().orElseThrow(() -> new PlayerNotFoundException(String.format("Player with merchant reference: %s not found", depositDTO.getTransactionReference())));
		Player player = gson.fromJson(playerMapping.getPayload(), Player.class);

		if (depositResponse.isPresent()) {
        	// Happy days, the deposit was processed successfully and wallet updated
            transactionService.createDepositGMRequestTx(depositDTO.getTransactionReference(), player.getId(), depositResponse.get());
            player.setDepositCount(player.getDepositCount() + 1);
			PlayerEvent playerEvent = PlayerEvent.builder().merchantReference(depositDTO.getTransactionReference())
					.player(player).build();
			paymentsHandler.updatePlayer(playerEvent);
			paymentsHandler.pushNotification(PushNotification.builder().inApp(true).emailAddress(player.getEmail())
					.content("Successfully deposited " + depositDTO.getRequestAmount() + " " +
							depositDTO.getRequestCurrency() + " to game account").build());
        } else {
        	// Unhappy days, create transactions and they'll get picked up in the retry job
            transactionService.createDepositGMRequestTx(depositDTO.getTransactionReference(), player.getId());
            transactionService.createDepositGMResponseTx(depositDTO.getTransactionReference(), player.getId(), TransactionStatus.GM_RETRY, depositResponse);
        }
		return depositResponse;
	}

	public Optional<GmWithdrawResponse> executeWithdrawJob(TransactionReferenceDto dto) {
		Optional<GmWithdrawResponse> depositResponse = paymentClient.proceedWithdrawFromGameAccount(dto);
		PlayerMapping playerMapping = playerRepository.findByKeyValue(String.valueOf(dto.getTransactionReference()))
				.stream().findFirst().orElseThrow(() -> new PlayerNotFoundException(String.format("Player with merchant reference: %s not found", dto.getTransactionReference())));
		Player player = gson.fromJson(playerMapping.getPayload(), Player.class);

		if (depositResponse.isPresent()) {
			// Happy days, the withdraw was processed successfully and wallet updated
			transactionService.createWithdrawProceedRequestTx(dto.getTransactionReference(), player.getId());
			player.setWithdrawCount(player.getWithdrawCount() + 1);
			PlayerEvent playerEvent = PlayerEvent.builder().merchantReference(dto.getTransactionReference())
					.player(player).build();
			paymentsHandler.updatePlayer(playerEvent);
			paymentsHandler.pushNotification(PushNotification.builder().inApp(true).emailAddress(player.getEmail())
					.content("Successfully withdrawn funds from game account").build());
		} else {
			// Unhappy days, create transactions and they'll get picked up in the retry job
			transactionService.createWithdrawProceedRequestTx(dto.getTransactionReference(), player.getId());
		}
		return depositResponse;
	}

}
