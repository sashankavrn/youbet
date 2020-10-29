package com.youbetcha.converter;

import com.youbetcha.model.Player;
import com.youbetcha.model.dto.PlayerDto;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserToPlayerDtoConverterTest {

    PlayerToPlayerDtoConverter converter;

    @Before
    public void setUp() {
        converter = new PlayerToPlayerDtoConverter();
    }

    @Test
    public void shouldConvertFromUserToUserDto() {
        Player player = TestDataHelper.createUser();
        PlayerDto convert = converter.convert(player);

        assertEquals(player.getEmail(), convert.getEmail());
    }

}