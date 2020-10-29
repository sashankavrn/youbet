package com.youbetcha.service.handler;

import com.youbetcha.model.Player;
import com.youbetcha.model.event.OnPasswordChangeSuccessEvent;
import com.youbetcha.model.event.OnRegistrationCompleteEvent;
import com.youbetcha.model.event.OnResetPasswordEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Service
public class ApplicationEventPublisherHandler {

    @Value("${service.host}")
    private String host;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void constructConfirmRegistrationEvent(Player player) {
        Locale locale = request.getLocale();
        OnRegistrationCompleteEvent event = new OnRegistrationCompleteEvent(player, locale, host);
        eventPublisher.publishEvent(event);
    }

    public void constructResetPasswordEvent(Player player) {
        Locale locale = request.getLocale();
        OnResetPasswordEvent event = new OnResetPasswordEvent(player, locale, host);
        eventPublisher.publishEvent(event);
    }

    public void constructPasswordChangeSuccessEvent(Player player) {
        Locale locale = request.getLocale();
        OnPasswordChangeSuccessEvent event = new OnPasswordChangeSuccessEvent(player, locale, host);
        eventPublisher.publishEvent(event);
    }
}
