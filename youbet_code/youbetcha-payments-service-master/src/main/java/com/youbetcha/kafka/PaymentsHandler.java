package com.youbetcha.kafka;

import com.google.gson.Gson;
import com.youbetcha.model.PlayerAccount;
import com.youbetcha.model.event.AccountsEvent;
import com.youbetcha.model.event.PaymentEvent;
import com.youbetcha.model.event.PlayerEvent;
import com.youbetcha.model.event.PushNotification;
import com.youbetcha.model.mapping.AccountMapping;
import com.youbetcha.model.mapping.PlayerMapping;
import com.youbetcha.repository.AccountMappingRepository;
import com.youbetcha.repository.PlayerMappingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class PaymentsHandler {

    Gson gson = new Gson();

    private static final Logger logger = LoggerFactory.getLogger(PaymentsHandler.class);
    @Autowired
    private PlayerMappingRepository playerRepository;
    @Autowired
    private AccountMappingRepository accountsRepository;
    private PaymentsProcessor paymentsProcessor;

    @Autowired
    public PaymentsHandler(PaymentsProcessor paymentsProcessor) {
        this.paymentsProcessor = paymentsProcessor;
    }

    @StreamListener(PaymentsProcessor.PLAYER_SERVICE_PLAYER)
    public void handle(@Payload PlayerEvent playerEvent) {
        logger.info("Adding {}", playerEvent);
        addPlayer(playerEvent);
    }

    @StreamListener(PaymentsProcessor.PLAYER_SERVICE_ACCOUNTS)
    public void handle(@Payload AccountsEvent accountEvent) {
        logger.info("Adding {}", accountEvent);
        addAccounts(accountEvent);
    }

    public void pushNotification(PushNotification pushNotification) {
        logger.info("Pushing notification to notification service database = {}", pushNotification);
        paymentsProcessor.sendPushNotification().send(MessageBuilder.withPayload(pushNotification).build());
    }

    public void fetchUserDetails(PaymentEvent paymentEvent) {
        paymentsProcessor.fetchUserDetails().send(MessageBuilder.withPayload(paymentEvent).build());
    }

    public void updatePlayer(PlayerEvent playerEvent) {
        paymentsProcessor.updatePlayer().send(MessageBuilder.withPayload(playerEvent).build());
    }

    public void addPlayer(PlayerEvent playerEvent) {
        PlayerMapping playerMapping = PlayerMapping.builder()
                .keyValue(playerEvent.getMerchantReference())
                .payload(gson.toJson(playerEvent.getPlayer()))
                .build();
        playerRepository.save(playerMapping);
    }

    public void addAccounts(AccountsEvent accountsEvent) {
        AccountMapping accountMapping = AccountMapping.builder()
                .keyValue(accountsEvent.getMerchantReference())
                .payload(gson.toJson(constructPlayerAccount(accountsEvent)))
                .build();
        accountsRepository.save(accountMapping);
    }

    private PlayerAccount constructPlayerAccount(AccountsEvent accountsEvent) {
        return PlayerAccount.builder()
                .maxDepositResponse(accountsEvent.getMaxDepositResponse())
                .id(accountsEvent.getCreditAccountId())
                .build();
    }
}
