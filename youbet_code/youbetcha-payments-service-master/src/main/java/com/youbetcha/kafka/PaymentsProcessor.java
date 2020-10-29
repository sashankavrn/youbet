package com.youbetcha.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface PaymentsProcessor {

    String PLAYER_SERVICE_PLAYER = "player-player-payment-event";
    String PLAYER_SERVICE_ACCOUNTS = "player-user-accounts-payment-event";
    String PAYMENTS_SERVICE_USER_DETAILS = "payments-user-details-payment-event";
    String PAYMENTS_UPDATE_PLAYER = "payments-update-player-event";
    String PUSH_NOTIFICATION = "push_notification";

    @Input(PLAYER_SERVICE_ACCOUNTS)
    SubscribableChannel sourceAccounts();

    @Input(PLAYER_SERVICE_PLAYER)
    SubscribableChannel sourcePlayer();

    @Output(PAYMENTS_SERVICE_USER_DETAILS)
    MessageChannel fetchUserDetails();

    @Output(PAYMENTS_UPDATE_PLAYER)
    MessageChannel updatePlayer();

    @Output(PUSH_NOTIFICATION)
    MessageChannel sendPushNotification();
}
