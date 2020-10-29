package com.youbetcha.converter;

import com.youbetcha.model.entity.Game;
import com.youbetcha.model.games.GameDto;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GameToGameDtoConverterTest {

    GameToGameDtoConverter gameToGameDtoConverter;

    @Before
    public void setUp() {
        gameToGameDtoConverter = new GameToGameDtoConverter();
    }

    @Test
    public void shouldConvertFromGameToGameDto() {
        Game game = TestDataHelper.createGame();
        GameDto gameDto = gameToGameDtoConverter.convert(game);

        assertNotNull(gameDto);
        assertEquals(game.getSlug(), gameDto.getSlug());
    }
}
