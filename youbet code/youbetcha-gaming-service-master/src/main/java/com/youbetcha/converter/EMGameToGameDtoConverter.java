package com.youbetcha.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.youbetcha.model.entity.EMGame;
import com.youbetcha.model.entity.GameProperties;
import com.youbetcha.model.games.GameDto;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EMGameToGameDtoConverter implements Converter<EMGame, GameDto> {

    @Override
    public GameDto convert(EMGame source) {
        Gson gson = new Gson();
        GameDto gameDto = new GameDto();
        gameDto.setSlug(source.getSlug());
        gameDto.setProperties(gson.fromJson(source.getGameProperties(), GameProperties.class));
        return gameDto;
    }
}
