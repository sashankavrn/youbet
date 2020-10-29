package com.youbetcha.model.event;

import com.youbetcha.model.Player;

import java.util.Locale;

public class OnPasswordChangeSuccessEvent extends Event {
    public OnPasswordChangeSuccessEvent(Player player, Locale locale, String appUrl) {
        super(player, locale, appUrl);
    }
}
