package com.youbetcha.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.youbetcha.model.games.GameDto;
import com.youbetcha.model.games.MixDataResponse;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class MixDataResponseToGameDtoConverter implements Converter<MixDataResponse, GameDto> {
    @Override
    public GameDto convert(MixDataResponse mixDataResponse) {
        GameDto gameDto = new GameDto();
        if (mixDataResponse.getData() != null) {
            gameDto.setSlug(mixDataResponse.getData().getSlug());
        }
        return gameDto;
    }
}


