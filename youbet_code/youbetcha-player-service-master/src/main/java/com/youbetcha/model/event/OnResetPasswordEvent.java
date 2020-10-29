package com.youbetcha.model.event;

import com.youbetcha.model.Player;

import java.util.Locale;

public class OnResetPasswordEvent extends Event {

    public OnResetPasswordEvent(Player player, Locale locale, String appUrl) {
        super(player, locale, appUrl);
    }
}
