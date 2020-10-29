package com.youbetcha.converter;

import com.google.gson.Gson;
import com.youbetcha.model.entity.Game;
import com.youbetcha.model.entity.GameProperties;
import com.youbetcha.model.games.GameDto;
import com.youbetcha.model.games.YBCategory;

import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GameToGameDtoConverter implements Converter<Game, GameDto> {

    Gson gson = new Gson();

    public GameToGameDtoConverter() {
    }

    @Override
    public GameDto convert(Game source) {
        GameDto gameDto = new GameDto();
        gameDto.setId(source.getId());
        if (source.getCategory().equals(YBCategory.LIVE_CASINO.getYBCategoryValue())) {
        	gameDto.setTableId(source.getDataId());
        }
        gameDto.setSlug(source.getSlug());
        gameDto.setOrderNumber(source.getOrderNumber());
        gameDto.setProperties(gson.fromJson(source.getGameProperties(), GameProperties.class));

        return gameDto;
    }
}
