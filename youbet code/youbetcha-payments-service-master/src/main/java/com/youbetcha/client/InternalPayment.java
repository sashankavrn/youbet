package com.youbetcha.client;

import java.util.Optional;

import com.youbetcha.model.Player;

public interface InternalPayment {
	Optional<Player> getPlayer(Long id);

	Optional<Player> getPlayer(String sessionId);
}
