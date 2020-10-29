package com.youbetcha.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.youbetcha.model.entity.Game;
import com.youbetcha.model.entity.Tag;
import com.youbetcha.model.games.GameDto;
import com.youbetcha.model.games.TagDto;
import com.youbetcha.model.games.Terminal;
import com.youbetcha.model.games.YBCategory;
import com.youbetcha.repository.GameOrderingRepository;
import com.youbetcha.repository.GameRepository;

@Service
public class GameLobbyService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GameLobbyService.class);

    @Autowired
    GameRepository gameRepository;
    
    @Autowired
    GameOrderingRepository gameOrderingRepository;

    @Autowired
    ConversionService conversionService;

    public Map<TagDto, List<GameDto>> getGamesByCategory(String countryCode, YBCategory category, Terminal terminal, Pageable pageable) {
    	
    	List<Object[]> queryList = gameOrderingRepository.findActiveGamesByCountryAndCategoryAndTerminal(countryCode, category.getYBCategoryValue(), terminals(terminal), pageable);
//        LOGGER.debug("D> Returned {} games for category {} and country {}", queryList.size(), category, countryCode);

    	Map<TagDto, List<GameDto>> allGames = new HashMap<TagDto, List<GameDto>>();
    	for(Object[] o : queryList) {
    		Game g = (Game) o[0];
    		Integer order = (Integer) o[1];
    		Tag tag = (Tag) o[2];
//    		LOGGER.debug("D> Setting order number {} to game id {}", order, g.getId());
    		g.setOrderNumber(order);
    		for (Tag t : g.getTags()) {
    			if (t.getId() == tag.getId()) {
    				TagDto tagDto = conversionService.convert(t, TagDto.class);
            		List<GameDto> gameList = allGames.get(tagDto);
            		// If the tag hasn't been added yet, the list will be null, so initialise here
            		if (gameList == null) {
            			gameList = new ArrayList<GameDto>();
            		}
            		if (!gameAdded(g.getId(), gameList)) {
            			GameDto gameDto = conversionService.convert(g, GameDto.class);
            			gameList.add(gameDto);
            			allGames.put(tagDto, gameList);
            		}
    			}
    		}
    	}
    	return allGames;
    }
    
    // Determine if the game ID already exists in this list - this will remove any duplicate games for a given tag
    private boolean gameAdded(Long id, List<GameDto> gameList) {
    	for (GameDto g : gameList) {
    		if (g.getId() == id) {
    			return true;
    		}
    	}
		return false;
	}

	public List<GameDto> getGamesByTag(String countryCode, String tag, Terminal terminal, Pageable pageable) {
		// size/pageable is not in use yet - it's here in case of a future need where we want to limit the number of games by tag
    	List<Object[]> queryList = gameOrderingRepository.findActiveGamesByCountryAndTagAndTerminal(countryCode, tag, terminals(terminal));
      LOGGER.debug("D> Returned {} games for tag {} and country {}", queryList.size(), tag, countryCode);
	
	  	List<GameDto> allGames = new ArrayList<GameDto>();
	  	for(Object[] o : queryList) {
	  		Game g = (Game) o[0];
	  		Integer order = (Integer) o[1];
	  		LOGGER.debug("D> Setting order number {} to game id {}", order, g.getId());
	  		g.setOrderNumber(order);
    		if (!gameAdded(g.getId(), allGames)) {
    			GameDto gameDto = conversionService.convert(g, GameDto.class);
    			allGames.add(gameDto);
    		}
	  	}
	  	return allGames;
    }
    
    private List<String> terminals(Terminal terminal) {
        return Arrays.asList(terminal.name(), Terminal.CROSS_PLATFORM.name());
    }
}
