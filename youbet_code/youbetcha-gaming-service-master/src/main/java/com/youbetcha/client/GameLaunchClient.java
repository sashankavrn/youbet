package com.youbetcha.client;

import com.youbetcha.model.games.GameLaunchDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.lang.String.format;

@Component
@Profile("!ci")
public class GameLaunchClient implements GameLaunch {

    static final String GAME_LAUNCH_URL = "%s/%s/%s/?_sid=%s&funMode=%s&casinolobbyurl=%s&cashierurl=%s";
    static final String TABLE_GAME_LAUNCH_URL = "%s/%s/%s/?tableID=%s&_sid=%s&casinolobbyurl=%s&cashierurl=%s";
    // FIXME - paramatise the hostname for our own environment
    static final String CASHIER_URL = "https://beta.youbetcha.com/centraluser/wallet";
    static final String DEFAULT_LOBBY_URL = "https://beta.youbetcha.com/";
    private static final Logger LOGGER = LoggerFactory.getLogger(GameLaunchClient.class);
    private String hostName;
    private String domainId;
    private CustomHttpClient customHttpClient;

    public GameLaunchClient(CustomHttpClient customHttpClient,
                      @Value("${everymatrix.game-launch.host-name}") String hostName,
                      @Value("${everymatrix.game-launch.domain-id}") String domainId) {
        this.domainId = domainId;
        this.customHttpClient = customHttpClient;
        this.hostName = hostName;
    }

    @Override
    public Optional<String> launchGame(GameLaunchDto gameLaunchDto, String sessionId) {
    	if (gameLaunchDto.getLobbyURL() == null || gameLaunchDto.getLobbyURL().isEmpty()) {
    		gameLaunchDto.setLobbyURL(DEFAULT_LOBBY_URL);
    	}
        String url = format(GAME_LAUNCH_URL, hostName, domainId, gameLaunchDto.getSlug(), sessionId, gameLaunchDto.isFunmode(), gameLaunchDto.getLobbyURL(), CASHIER_URL);
        LOGGER.info("Game launch url is {}", url);
        return Optional.of(url);
    }

    @Override
    public Optional<String> launchTableGame(GameLaunchDto gameLaunchDto, String sessionId) {
    	if (gameLaunchDto.getLobbyURL() == null || gameLaunchDto.getLobbyURL().isEmpty()) {
    		gameLaunchDto.setLobbyURL(DEFAULT_LOBBY_URL);
    	}
        String url = format(TABLE_GAME_LAUNCH_URL, hostName, domainId, gameLaunchDto.getSlug(), gameLaunchDto.getTableId(), sessionId, gameLaunchDto.getLobbyURL(), CASHIER_URL);
        LOGGER.info("Table game launch url is {}", url);
        return Optional.of(url);
    }
}
