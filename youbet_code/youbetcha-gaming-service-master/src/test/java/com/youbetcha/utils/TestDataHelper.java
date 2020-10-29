package com.youbetcha.utils;

import com.youbetcha.model.ErrorData;
import com.youbetcha.model.SessionIdValidationResponse;
import com.youbetcha.model.entity.*;
import com.youbetcha.model.games.GameDto;
import com.youbetcha.model.games.Terminal;
import com.youbetcha.model.games.YBCategory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class TestDataHelper {

    private static final String TIMESTAMP = "2019-02-12 15:12:06";

    public static EMGame createEMGame() {
        EMGame emGame = new EMGame();
        emGame.setDataId(100456L);
        emGame.setGameId("Bobby7sFlash");
        emGame.setSlug("bobby-7s");
        emGame.setGameName("bobby7s");
        emGame.setRestrictedTerritories("AE,DF,GB");
        emGame.setCategories("VIDEOSLOTS");
        return emGame;
    }

    public static Game createGame() {
        Game game = new Game();
        game.setDataId(100456L);
        game.setGameId("Bobby7sFlash");
        game.setSlug("bobby-7s");
        game.setGameName("bobby7s");
        game.setRestrictedTerritories("AE,DF,GB");
        game.setIsOpen(Boolean.TRUE);
        game.setCategory(YBCategory.VIDEO_POKER.getYBCategoryValue());
        return game;
    }

    public static GameDto createGameDto() {
        GameDto game = new GameDto();
        game.setOrderNumber(1);
        game.setSlug("bobby-7s");
        GameProperties properties = new GameProperties();
        properties.setBackgroundImage("backgroundImage.jpg");
        game.setProperties(properties);
        return game;
    }

    public static EMTable createTable() {
        EMTable EMTable = new EMTable();
        EMTable.setTableId(8340032560L);
        EMTable.setGameId("three-card-poker-evo");
        EMTable.setSlug("three-card-poker-evo");
        EMTable.setTableName("Three Card Poker");
        EMTable.setRestrictedTerritories("AE,DF,GB");
        EMTable.setCategories("VIDEOSLOTS");
        EMTable.setCategory("POKER");
        EMTable.setTerminal(Terminal.MOBILE.name());
        return EMTable;
    }

    public static SessionIdValidationResponse createSessionIdSuccessResponse() {
        return SessionIdValidationResponse.builder()
                .errorData(generateSuccessfulLoginErrorData())
                .success((short) 1)
                .lastName("user")
                .firstName("test")
                .language("es")
                .version("1.0")
                .userId(1001001L)
                .timestamp(TIMESTAMP)
                .build();
    }

    private static ErrorData generateSuccessfulLoginErrorData() {
        return ErrorData.builder()
                .errorCode((short) 0)
                .errorMessage("")
                .logId(0)
                .build();
    }

    public static Pageable createPageable() {
        Pageable pageable = PageRequest.of(0, 8);
        return pageable;
    }
}
