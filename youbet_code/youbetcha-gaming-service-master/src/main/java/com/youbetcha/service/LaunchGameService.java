package com.youbetcha.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youbetcha.client.GameLaunch;
import com.youbetcha.exceptions.GameException;
import com.youbetcha.model.games.GameLaunchDto;

@Service
public class LaunchGameService {

    @Autowired
    GameLaunch game;
    
    public String launchCasinoGame(GameLaunchDto gameLaunchDto, String sessionId) {
		return game.launchGame(gameLaunchDto, sessionId)
				.orElseThrow(() -> new GameException("Game Launch error"));
    }
    
    public String launchTableGame(GameLaunchDto gameLaunchDto, String sessionId) {
		return game.launchTableGame(gameLaunchDto, sessionId)
				.orElseThrow(() -> new GameException("Table game Launch error"));
    }

}
