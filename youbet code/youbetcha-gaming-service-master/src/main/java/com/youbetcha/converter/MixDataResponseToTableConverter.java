package com.youbetcha.converter;

import com.google.gson.Gson;
import com.youbetcha.model.entity.EMTable;
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
public class MixDataResponseToTableConverter implements Converter<MixDataResponse, EMTable> {

    Gson gson = new Gson();

    @Override
    public EMTable convert(MixDataResponse source) {
        EMTable emTable = new EMTable();
        if (source.getData() != null) {
            emTable.setGameId(source.getData().getGameID());
            emTable.setSlug(source.getData().getSlug());
            emTable.setTableId(source.getId());
            emTable.setCategories(source.getData().getCategories().stream().collect(Collectors.joining(",")));
            emTable.setRestrictedTerritories(source.getData().getRestrictedTerritories().stream().collect(Collectors.joining(",")));
            //set the properties
            GameProperties tableProperties = new GameProperties();
            tableProperties.setFunGameURL(source.getData().getUrl());
            tableProperties.setThumbnail(source.getData().getPresentation().getThumbnail().getValue());
            tableProperties.setBackgroundImage(source.getData().getPresentation().getBackgroundImage().getValue());
            tableProperties.setLogo(source.getData().getPresentation().getLogo().getValue());
            emTable.setTableProperties(gson.toJson(tableProperties));
            emTable.setTableName(source.getData().getPresentation().getTableName().getValue());
            emTable.setCategory(source.getData().getCategory());
            emTable.setEnabled(source.getData().isEnabled());
            emTable.setOpen(source.getData().isOpen());
            emTable.setTerminal(source.getData().getProperty().getTerminal().stream().collect(Collectors.joining(",")));
            emTable.setUpdatedTs(Timestamp.from(Instant.now()));
        }
        return emTable;
    }
}
