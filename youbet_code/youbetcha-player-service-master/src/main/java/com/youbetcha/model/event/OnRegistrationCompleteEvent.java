package com.youbetcha.model.event;

import com.youbetcha.model.Player;

import java.util.Locale;

public class OnRegistrationCompleteEvent extends Event {

    public OnRegistrationCompleteEvent(Player player, Locale locale, String appUrl) {
        super(player, locale, appUrl);
    }
}
