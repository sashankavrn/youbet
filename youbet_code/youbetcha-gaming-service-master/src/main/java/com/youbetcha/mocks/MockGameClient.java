package com.youbetcha.mocks;

import com.youbetcha.client.GameLaunch;
import com.youbetcha.model.games.GameLaunchDto;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Profile("ci")
public class MockGameClient implements GameLaunch {
    @Override
    public Optional<String> launchGame(GameLaunchDto dto, String sessionId) {
        return Optional.empty();
    }

    @Override
    public Optional<String> launchTableGame(GameLaunchDto dto, String sessionId) {
        return Optional.empty();
    }
}
