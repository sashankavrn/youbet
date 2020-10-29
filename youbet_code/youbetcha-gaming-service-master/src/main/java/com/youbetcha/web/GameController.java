package com.youbetcha.web;

import java.security.Principal;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.youbetcha.jwt.JwtTokenUtil;
import com.youbetcha.model.entity.Game;
import com.youbetcha.model.entity.GameProperties;
import com.youbetcha.model.games.GameLaunchDto;
import com.youbetcha.service.GameService;
import com.youbetcha.service.LaunchGameService;

import io.jsonwebtoken.MalformedJwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/games")
@Api(tags = "Games")
public class GameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    @Autowired
    LaunchGameService launchGameService;
    
    @Autowired
    GameService gameService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping()
    @ApiOperation(value = "Launch game")
    public ResponseEntity<String> launchGame(@RequestBody GameLaunchDto dto,
                                             @CurrentSecurityContext(expression = "authentication.principal") Principal principal) {
        LOGGER.info("Attempting to launch game {} with principal {}", dto.toString(), principal.getName());
        // User is not logged in, launch the game in funMode = true only
        if (principal != null && principal.getName() != null && !principal.getName().isEmpty()) {
        	String sessionId;
			try {
				sessionId = jwtTokenUtil.getSessionIdFromToken(principal.getName());
				String gameUrl = "";
				if (Objects.nonNull(dto.getTableId())) {
					gameUrl = launchGameService.launchTableGame(dto, sessionId);
				} else {
					gameUrl = launchGameService.launchCasinoGame(dto, sessionId);
				}
				return new ResponseEntity<>(gameUrl, HttpStatus.OK);
			} catch (MalformedJwtException e) {

				LOGGER.warn("W> JWT parse exception launching game slug {} with principal name {}", dto.getSlug(), principal.getName());
	        	return returnFunGame(dto);
			}
			
        } else {
        	return returnFunGame(dto);
        }
    }

	private ResponseEntity<String> returnFunGame(GameLaunchDto dto) {
		Game game = gameService.findGameBySlug(dto.getSlug());
		return new ResponseEntity<>(parseGameName(game.getGameProperties()), HttpStatus.OK);
	}

    //{"funGameURL":"https://gamelaunch-stage.everymatrix.com/Loader/Start/2207/ezugi-roulette/?tableID\u003d8682037739","thumbnail":"//static.everymatrix.com/cms2/base/_casino/5/5A25AC6E7A7369202578EBD909CE8C6D.jpg"}
	private String parseGameName(String gameProperties) {
		Gson gson = new Gson();
		GameProperties properties = gson.fromJson(gameProperties, GameProperties.class);
		return properties.getFunGameURL();
	}
    
}
