package com.youbetcha.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface PlayerMessageProcessor {

    String PLAYER_SERVICE_PLAYER = "player-player-payment-event";
    String PLAYER_SERVICE_ACCOUNTS = "player-user-accounts-payment-event";
    String PAYMENTS_SERVICE_USER_DETAILS = "payments-user-details-payment-event";
    String PAYMENTS_UPDATE_PLAYER = "payments-update-player-event";
    String PLAYER_VERIFY_PLAYER = "verify-player";
    String PLAYER_RESET_PASSWORD = "reset-player-password";
    String PLAYER_CHANGE_PASSWORD_SUCCESS = "password-change-success";
    String PUSH_NOTIFICATION = "push_notification";

    @Output(PLAYER_SERVICE_ACCOUNTS)
    MessageChannel sourceAccounts();

    @Output(PLAYER_SERVICE_PLAYER)
    MessageChannel sourcePlayer();

    @Input(PAYMENTS_SERVICE_USER_DETAILS)
    SubscribableChannel fetchUserDetails();

    @Input(PAYMENTS_UPDATE_PLAYER)
    SubscribableChannel updatePlayer();

    @Output(PLAYER_VERIFY_PLAYER)
    MessageChannel sendVerifyPlayer();

    @Output(PLAYER_RESET_PASSWORD)
    MessageChannel sendResetPassword();

    @Output(PLAYER_CHANGE_PASSWORD_SUCCESS)
    MessageChannel sendChangePasswordSuccess();

    @Output(PUSH_NOTIFICATION)
    MessageChannel sendPushNotification();
}
