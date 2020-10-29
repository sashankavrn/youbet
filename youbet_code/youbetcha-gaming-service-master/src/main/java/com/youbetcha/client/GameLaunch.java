package com.youbetcha.client;

import com.youbetcha.model.games.GameLaunchDto;

import java.util.Optional;

public interface GameLaunch {
    Optional<String> launchGame(GameLaunchDto dto, String sessionId);

    Optional<String> launchTableGame(GameLaunchDto dto, String sessionId);
}
