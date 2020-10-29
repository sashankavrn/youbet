package com.youbetcha.model.event;

import com.youbetcha.model.Player;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
public class Event extends ApplicationEvent {

    private final String appUrl;
    private final Locale locale;
    private final Player player;

    public Event(final Player player, final Locale locale, final String appUrl) {
        super(player);
        this.player = player;
        this.locale = locale;
        this.appUrl = appUrl;
    }

}
