package com.youbetcha.converter;

import com.youbetcha.model.Player;
import com.youbetcha.model.dto.PlayerDto;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerDtoToPlayerConverterTest {

    PlayerDtoToPlayerConverter converter;

    @Before
    public void setUp() {
        converter = new PlayerDtoToPlayerConverter();
    }

    @Test
    public void shouldConvertFromUserToUserDto() {
        PlayerDto playerDto = TestDataHelper.createUserDto();
        Player convert = converter.convert(playerDto);

        assertEquals(playerDto.getEmail(), convert.getEmail());
    }

}