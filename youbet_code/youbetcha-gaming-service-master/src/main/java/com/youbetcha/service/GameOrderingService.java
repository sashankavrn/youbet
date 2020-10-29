package com.youbetcha.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.youbetcha.model.entity.GameOrdering;
import com.youbetcha.model.games.Terminal;
import com.youbetcha.repository.GameOrderingRepository;

@Service
public class GameOrderingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameOrderingService.class);

    private final GameOrderingRepository gameOrderingRepository;

    @Autowired
    public GameOrderingService(GameOrderingRepository gameOrderingRepository) {
        this.gameOrderingRepository = gameOrderingRepository;
    }

    
    public List<GameOrdering> getGameOrderingsByTagAndCountryAndTerminal(String tag, String countryCode, String terminal) {
    	return gameOrderingRepository.findActiveGameOrderingsByCountryAndTagAndTerminal(countryCode, tag, terminals(Terminal.valueOf(terminal)));
    }
    
    public List<GameOrdering> updateGameOrderingsByTagAndCountryAndTerminal(List<GameOrdering> gameOrderings) {
    	// We're getting in an unordered list with no IDs, so let's look for any existing ones and do updates if we can
    	List<GameOrdering> gosToUpdate = new ArrayList<GameOrdering>();
    	for (GameOrdering go : gameOrderings) {
    		Optional<GameOrdering> existingGO = gameOrderingRepository.findGameOrderingByGameIdAndTagIdAndCountryCode(go.getGame().getId(), go.getTag().getId(), go.getCountryCode());
    		if (existingGO.isPresent()) {
//    			LOGGER.info("D> Found existing GO id {} with game {} tag {} and country {} and order {}", existingGO.get().getId(), go.getGame().getId(), go.getTag().getId(), go.getCountryCode(), go.getOrderNumber());
    			GameOrdering matchingGO = findMatchingGameOrdering(gameOrderings, go.getGame().getId(), go.getTag().getId(), go.getCountryCode());
    			GameOrdering newGO = existingGO.get();
    			matchingGO.setId(newGO.getId());
    			matchingGO.setOrderNumber(go.getOrderNumber());
    			matchingGO.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
    			gosToUpdate.add(matchingGO);
//    			LOGGER.info("D> Persisting new order of {} to new GO: {}", go.getOrderNumber(), matchingGO.toString());
    		}
    	}
    	
    	// Now we have our existing GO entities to update, we need to add the newly added ones
    	for (GameOrdering go : gameOrderings) {
    		GameOrdering matchedGO = findMatchingGameOrdering(gosToUpdate, go.getGame().getId(), go.getTag().getId(), go.getCountryCode());
    		if (matchedGO == null) {
    			gosToUpdate.add(go);
    		}
    	}
    	
    	return gameOrderingRepository.saveAll(gosToUpdate);
    }
    
    private GameOrdering findMatchingGameOrdering(List<GameOrdering> gameOrderings, Long gameId, Long tagId,
			String countryCode) {
    	for (GameOrdering go : gameOrderings) {
    		if (go.getCountryCode().equalsIgnoreCase(countryCode) 
    				&& go.getGame().getId() == gameId
    				&& go.getTag().getId() == tagId) {
    			return go;
    		}
    	}
		return null;
	}


	private List<String> terminals(Terminal terminal) {
        return Arrays.asList(terminal.name(), Terminal.CROSS_PLATFORM.name());
    }
    
}
