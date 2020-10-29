package com.youbetcha.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.youbetcha.model.entity.EMTable;
import com.youbetcha.model.entity.GameProperties;
import com.youbetcha.model.games.GameDto;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TableToGameDtoConverter implements Converter<EMTable, GameDto> {
    Gson gson = new Gson();

    @Override
    public GameDto convert(EMTable source) {
        GameDto gameDto = new GameDto();
        gameDto.setSlug(source.getSlug());
        gameDto.setProperties(gson.fromJson(source.getTableProperties(), GameProperties.class));
        return gameDto;
    }
}
