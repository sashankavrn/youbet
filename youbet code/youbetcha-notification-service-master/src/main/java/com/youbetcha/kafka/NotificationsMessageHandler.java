package com.youbetcha.kafka;

import com.youbetcha.model.NotificationDto;
import com.youbetcha.model.PushNotification;
import com.youbetcha.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class NotificationsMessageHandler {

    private static final Logger logger = LoggerFactory.getLogger(NotificationsMessageHandler.class);
    @Autowired
    private EmailService emailService;
    @Autowired
    private MessageService messageService;

    @StreamListener(NotificationsMessageProcessor.UPDATE_CONTACT_LIST)
    public void handleUpdateContactList(@Payload NotificationDto notificationDto) {
        logger.info("Updating contact list: {}", notificationDto);
        emailService.updateContactList(notificationDto);
    }

    @StreamListener(NotificationsMessageProcessor.PLAYER_VERIFY_PLAYER)
    public void handleVerifyPlayer(@Payload NotificationDto notificationDto) {
        logger.info("Sending verification email: {}", notificationDto);
        emailService.verifyPlayer(notificationDto);
    }

    @StreamListener(NotificationsMessageProcessor.PLAYER_RESET_PASSWORD)
    public void handlePasswordEventPasswordEvent(@Payload NotificationDto notificationDto) {
        logger.info("Sending reset password email: {}", notificationDto);
        emailService.resetPasswordEvent(notificationDto);
    }

    @StreamListener(NotificationsMessageProcessor.PLAYER_PASSWORD_CHANGE_SUCCESS)
    public void handleChangePasswordSuccess(@Payload NotificationDto notificationDto) {
        logger.info("Sending password change success email: {}", notificationDto);
        emailService.changePasswordSuccess(notificationDto);
    }

    @StreamListener(NotificationsMessageProcessor.PUSH_NOTIFICATION)
    public void handlePushNotification(@Payload PushNotification pushNotification) {
        logger.info("Receiving push notification: {}", pushNotification);
        messageService.createPushNotification(pushNotification);
    }
}
