package com.youbetcha.client;

import com.youbetcha.model.Player;
import java.util.Optional;

public interface Internal {
    Optional<Player> getPlayer(String email);
}
