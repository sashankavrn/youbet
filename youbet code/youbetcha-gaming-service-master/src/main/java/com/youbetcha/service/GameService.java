package com.youbetcha.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import com.youbetcha.client.GameFeedClient;
import com.youbetcha.exceptions.GameException;
import com.youbetcha.model.entity.EMGame;
import com.youbetcha.model.entity.EMTable;
import com.youbetcha.model.entity.Game;
import com.youbetcha.model.games.MixDataResponse;
import com.youbetcha.model.games.UpdateGameRequest;
import com.youbetcha.repository.EMGameRepository;
import com.youbetcha.repository.EMTableRepository;
import com.youbetcha.repository.GameRepository;

@Service
public class GameService {

    private final EMGameRepository emGameRepository;
    private final EMTableRepository EMTableRepository;
    private final GameRepository gameRepository;
    private final GameFeedClient gameLobby;
    private final ConversionService conversionService;

    @Autowired
    public GameService(EMGameRepository emGameRepository,
                       EMTableRepository EMTableRepository,
                       GameRepository gameRepository, GameFeedClient gameLobby,
                       ConversionService conversionService) {
        this.emGameRepository = emGameRepository;
        this.EMTableRepository = EMTableRepository;
        this.gameRepository = gameRepository;
        this.gameLobby = gameLobby;
        this.conversionService = conversionService;
    }

    public Optional<List<MixDataResponse>> getMixData(String queryUrl) {
        return gameLobby.getMixData(queryUrl);
    }

    public void updateGames() {
        getMixData("Game").get().stream()
                .filter(s -> s.getData() != null)
                .map(r -> conversionService.convert(r, EMGame.class))
                .forEach(g -> {
                    //get existing EM entry
                    EMGame emGame = emGameRepository.findByDataId(g.getDataId()).orElse(g);
                    //update and save
                    g.setId(emGame.getId());
                    emGameRepository.save(g);
                    //get existing Game entry
                    Game game = gameRepository.findByDataId(g.getDataId()).orElse(conversionService.convert(g, Game.class));
                    gameRepository.save(game);
                });
    }

    public void updateTables() {
        getMixData("Table").get().stream()
                .filter(s -> s.getData() != null)
                .map(r -> conversionService.convert(r, EMTable.class))
                .forEach(t -> {
                    //get existing entry
                    EMTable emTable = EMTableRepository.findByTableId(t.getTableId()).orElse(t);
                    //update and save
                    t.setId(emTable.getId());
                    EMTableRepository.save(t);
                    //get existing Game entry
                    Game game = gameRepository.findByDataId(t.getTableId()).orElse(conversionService.convert(t, Game.class));
                    gameRepository.save(game);
                });
    }

    public Game updateGame(Long id, UpdateGameRequest updateGameRequest) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameException("Cannot find game with id: " + id));

        if (Objects.nonNull(updateGameRequest.getCategory())) {
            game.setCategory(updateGameRequest.getCategory());
        }
        if (Objects.nonNull(updateGameRequest.getGameName())) {
            game.setGameName(updateGameRequest.getGameName());
        }
        if (Objects.nonNull(updateGameRequest.getEnabled())) {
            game.setEnabled(updateGameRequest.getEnabled());
        }
        if (Objects.nonNull(updateGameRequest.getIsNew())) {
            game.setIsNew(updateGameRequest.getIsNew());
        }
        if (Objects.nonNull(updateGameRequest.getIsJackPot())) {
            game.setIsJackpot(updateGameRequest.getIsJackPot());
        }
        if (Objects.nonNull(updateGameRequest.getIsHot())) {
            game.setIsHot(updateGameRequest.getIsHot());
        }
        if (Objects.nonNull(updateGameRequest.getIsExclusive())) {
            game.setIsExclusive(updateGameRequest.getIsExclusive());
        }
        if (Objects.nonNull(updateGameRequest.getTags())) {
        	game.setTags(updateGameRequest.getTags());
        }

        game = gameRepository.save(game);
        return game;
    }

    public Game findGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new GameException("Cannot find game with id: " + id));
        return game;
    }
    
    public List<Game> findAllGames() {
    	return gameRepository.findAll();
    }

    public Game findGameBySlug(String slug) {
        Game game = gameRepository.findBySlug(slug)
                .orElseThrow(() -> new GameException("Cannot find game with slug: " + slug));
        return game;
    }
}
