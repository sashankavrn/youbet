package com.youbetcha.service.listener;

import com.google.gson.Gson;
import com.youbetcha.kafka.PlayerMessageHandler;
import com.youbetcha.model.Player;
import com.youbetcha.model.dto.NotificationDto;
import com.youbetcha.model.event.Event;
import com.youbetcha.model.event.OnPasswordChangeSuccessEvent;
import com.youbetcha.model.event.OnRegistrationCompleteEvent;
import com.youbetcha.model.event.OnResetPasswordEvent;
import com.youbetcha.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PlayerEventListener implements ApplicationListener<Event> {

    Gson gson = new Gson();

    @Autowired
    private VerificationService verificationService;
    @Autowired
    private PlayerMessageHandler handler;

    @Override
    public void onApplicationEvent(Event event) {
        this.init(event);
    }

    private void init(Event event) {
        final Player player = event.getPlayer();
        final String token = UUID.randomUUID().toString();
        verificationService.createVerificationTokenForUser(player, token);

        NotificationDto dto = NotificationDto.builder().appUrl(event.getAppUrl())
                .email(player.getEmail())
                .locale(event.getLocale())
                .token(token)
                .build();

        String data = gson.toJson(dto);

        if (event instanceof OnResetPasswordEvent) {
            handler.resetPassword(data);
        } else if (event instanceof OnRegistrationCompleteEvent) {
            handler.confirmAndNotify(data);
        } else if (event instanceof OnPasswordChangeSuccessEvent) {
            handler.passwordSuccess(data);
        }
    }

}
