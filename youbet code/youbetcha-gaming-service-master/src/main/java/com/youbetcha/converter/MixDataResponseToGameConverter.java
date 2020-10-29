package com.youbetcha.converter;

import com.google.gson.Gson;
import com.youbetcha.model.entity.EMGame;
import com.youbetcha.model.entity.GameProperties;
import com.youbetcha.model.games.MixDataResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class MixDataResponseToGameConverter implements Converter<MixDataResponse, EMGame> {

    Gson gson = new Gson();

    @Override
    public EMGame convert(MixDataResponse source) {
        EMGame emGame = new EMGame();
        //skip initial handshake value
        if (source.getData() != null) {
            emGame.setGameId(source.getData().getGameID());
            emGame.setSlug(source.getData().getSlug());
            emGame.setEnabled(source.getData().isEnabled());
            emGame.setDataId(source.getId());
            emGame.setCategories(source.getData().getCategories().stream().collect(Collectors.joining(",")));
            if (source.getData().getTags() != null) {
                emGame.setTags(source.getData().getTags().stream().collect(Collectors.joining(",")));
            }
            emGame.setRestrictedTerritories(source.getData().getRestrictedTerritories().stream().collect(Collectors.joining(",")));
            emGame.setPopularity(source.getData().getPopularity().getRanking());
            if (source.getData().getPresentation() != null) {
                if (source.getData().getPresentation().getGameName() != null) {
                    emGame.setGameName(source.getData().getPresentation().getGameName().getValue());
                }
                GameProperties gameProperties = new GameProperties();
                gameProperties.setFunGameURL(source.getData().getUrl());
                gameProperties.setThumbnail(source.getData().getPresentation().getThumbnail().getValue());
                gameProperties.setBackgroundImage(source.getData().getPresentation().getBackgroundImage().getValue());
                gameProperties.setLogo(source.getData().getPresentation().getLogo().getValue());
                emGame.setGameProperties(gson.toJson(gameProperties));
            }
            emGame.setTerminal(source.getData().getProperty().getTerminal().stream().collect(Collectors.joining(",")));

            emGame.setUpdatedTs(Timestamp.from(Instant.now()));
        }
        return emGame;
    }
}
