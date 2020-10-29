package com.youbetcha.kafka;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;

@Component
public interface NotificationsMessageProcessor {

    String PLAYER_RESET_PASSWORD = "reset-player-password";
    String PLAYER_VERIFY_PLAYER = "verify-player";
    String PLAYER_PASSWORD_CHANGE_SUCCESS = "password-change-success";
    String PUSH_NOTIFICATION = "push-notification";
    String UPDATE_CONTACT_LIST = "update-contact-list";

    @Input(PLAYER_RESET_PASSWORD)
    SubscribableChannel receiveResetPasswordEvent();

    @Input(PLAYER_VERIFY_PLAYER)
    SubscribableChannel receiveVerifyPlayer();

    @Input(PLAYER_PASSWORD_CHANGE_SUCCESS)
    SubscribableChannel receiveChangePasswordSuccess();

    @Input(PUSH_NOTIFICATION)
    SubscribableChannel receivePushNotification();

    @Input(UPDATE_CONTACT_LIST)
    SubscribableChannel receiveUpdateContactList();
}
