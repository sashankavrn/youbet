package com.youbetcha.converter;

import com.youbetcha.model.entity.EMTable;
import com.youbetcha.model.entity.Game;
import com.youbetcha.model.games.Terminal;
import com.youbetcha.model.games.YBCategory;
import lombok.AllArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

@Component
@AllArgsConstructor
public class EMTableToGameConverter implements Converter<EMTable, Game> {

    @Override
    public Game convert(EMTable source) {
        Game game = new Game();
        game.setDataId(source.getTableId());
        game.setSlug(source.getSlug());
        game.setGameId(source.getGameId());
        game.setGameName(source.getTableName());
        game.setCategory(YBCategory.transformEMToYBCategory(source.getCategories()).getYBCategoryValue());
        game.setEnabled(Boolean.valueOf(true)); //Boolean.valueOf(source.isEnabled())
        game.setIsOpen(Boolean.valueOf(source.isOpen()));
        game.setRestrictedTerritories(source.getRestrictedTerritories());
        game.setGameProperties(source.getTableProperties());
        game.setTerminal(Terminal.transformEMTerminalToDBType(source.getTerminal()).name());
        game.setIsNew(Boolean.valueOf(false));
        game.setIsJackpot(Boolean.valueOf(YBCategory.isEMGameIsJackPot(source.getCategories())));
        game.setIsHot(Boolean.valueOf(false));
        game.setIsExclusive(Boolean.valueOf(false));
        game.setUpdatedTs(Timestamp.from(Instant.now()));
        return game;
    }

}
