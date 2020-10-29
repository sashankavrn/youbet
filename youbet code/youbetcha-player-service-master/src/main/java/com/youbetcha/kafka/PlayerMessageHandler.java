package com.youbetcha.kafka;

import com.youbetcha.exceptions.AccountException;
import com.youbetcha.exceptions.JsonNullOrEmptyException;
import com.youbetcha.model.Accounts;
import com.youbetcha.model.Player;
import com.youbetcha.model.account.MaxDepositResponse;
import com.youbetcha.model.dto.SessionDto;
import com.youbetcha.model.event.AccountsEvent;
import com.youbetcha.model.event.PaymentEvent;
import com.youbetcha.model.event.PlayerEvent;
import com.youbetcha.model.event.PushNotification;
import com.youbetcha.service.AccountService;
import com.youbetcha.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class PlayerMessageHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerMessageHandler.class);

    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    private PlayerMessageProcessor playerMessageProcessor;

    @Autowired
    public PlayerMessageHandler(PlayerMessageProcessor playerMessageProcessor) {
        this.playerMessageProcessor = playerMessageProcessor;
    }

    @StreamListener(PlayerMessageProcessor.PAYMENTS_SERVICE_USER_DETAILS)
    public void handlePlayer(@Payload PaymentEvent paymentEvent) {
        fetchUserDetailsWithSessionId(paymentEvent);
    }

    @StreamListener(PlayerMessageProcessor.PAYMENTS_UPDATE_PLAYER)
    public void handlePlayer(@Payload PlayerEvent playerEvent) {
        updatePlayer(playerEvent);
    }

    private void updatePlayer(PlayerEvent playerEvent) {
        userService.updatePlayer(playerEvent.getPlayer());
    }

    public void confirmAndNotify(String notificationDto) {
        LOGGER.info("Sending registration confirmation notification dto = {}", notificationDto);
        playerMessageProcessor.sendVerifyPlayer().send(MessageBuilder.withPayload(notificationDto).build());
    }

    public void resetPassword(String notificationDto) {
        LOGGER.info("Sending reset password notification dto = {}", notificationDto);
        playerMessageProcessor.sendResetPassword().send(MessageBuilder.withPayload(notificationDto).build());
    }

    public void passwordSuccess(String notificationDto) {
        LOGGER.info("Sending password change success notification dto = {}", notificationDto);
        playerMessageProcessor.sendChangePasswordSuccess().send(MessageBuilder.withPayload(notificationDto).build());
    }

    public void pushNotification(PushNotification pushNotification) {
        LOGGER.info("Pushing notification to notification service database = {}", pushNotification);
        playerMessageProcessor.sendPushNotification().send(MessageBuilder.withPayload(pushNotification).build());
    }

    private void fetchUserDetailsWithSessionId(PaymentEvent paymentEvent) {
        sendPlayerToPayments(paymentEvent);
        sendAccountsToPayments(paymentEvent);
    }

    private void sendPlayerToPayments(PaymentEvent paymentEvent) {
        Player player = userService.userDetails(paymentEvent.getSessionId())
                .map(userDetailsResponse -> userService.getUserByEmail(userDetailsResponse.getEmail()))
                .orElseThrow(JsonNullOrEmptyException::new);
        PlayerEvent playerEvent = constructPlayerEvent(player, paymentEvent.getMerchantReference());
        playerMessageProcessor.sourcePlayer().send(MessageBuilder.withPayload(playerEvent).build());
    }

    private void sendAccountsToPayments(PaymentEvent paymentEvent) {
        Accounts accounts = accountService.getUserAccounts(paymentEvent.getSessionId()).getAccounts().stream()
                .filter(x -> x.getDisplayName().equalsIgnoreCase("casino"))
                .findFirst().orElseThrow(() -> new AccountException("Account with display name Casino not found"));
        Player player = userService.userDetails(paymentEvent.getSessionId())
                .map(userDetailsResponse -> userService.getUserByEmail(userDetailsResponse.getEmail()))
                .orElseThrow(JsonNullOrEmptyException::new);
        String everymatrixUserId = player.getEverymatrixUserId().toString();
        SessionDto sessionDto = SessionDto.builder()
                .userId(Long.parseLong(everymatrixUserId))
                .build();
        MaxDepositResponse maxDepositResponse = accountService.checkMaxDepositAmount(sessionDto);
        AccountsEvent accountsEvent = constructAccountsEvent(accounts, paymentEvent.getMerchantReference(), maxDepositResponse);
        playerMessageProcessor.sourceAccounts().send(MessageBuilder.withPayload(accountsEvent).build());
    }

    private PlayerEvent constructPlayerEvent(Player player, String merchantReference) {
        return PlayerEvent.builder()
                .player(player)
                .merchantReference(merchantReference)
                .build();
    }

    private AccountsEvent constructAccountsEvent(Accounts accounts, String merchantReference, MaxDepositResponse maxDepositResponse) {
        return AccountsEvent.builder()
                .creditAccountId(accounts.getId())
                .merchantReference(merchantReference)
                .maxDepositResponse(maxDepositResponse)
                .build();
    }
}
