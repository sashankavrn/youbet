package com.youbetcha.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youbetcha.model.entity.Game;
import com.youbetcha.model.entity.GameOrdering;
import com.youbetcha.model.entity.Tag;
import com.youbetcha.model.games.OrderingListRequest;
import com.youbetcha.model.games.OrderingRequest;
import com.youbetcha.model.games.UpdateGameRequest;
import com.youbetcha.model.games.tags.TagRequest;
import com.youbetcha.service.EMRefreshService;
import com.youbetcha.service.GameOrderingService;
import com.youbetcha.service.GameService;
import com.youbetcha.service.TagService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/admin/games")
@Api(tags = "Admin")
public class GameAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameAdminController.class);

    private final EMRefreshService emRefreshService;
    private final GameService gameService;
    private final GameOrderingService gameOrderingService;
    private final TagService tagService;
    private final ConversionService conversionService;

    @Autowired
    public GameAdminController(EMRefreshService emRefreshService, GameService gameService, GameOrderingService gameOrderingService, TagService tagService, ConversionService conversionService) {
        this.emRefreshService = emRefreshService;
        this.gameService = gameService;
        this.gameOrderingService = gameOrderingService;
        this.tagService = tagService;
        this.conversionService = conversionService;
    }

    @GetMapping("/game/refresh")
    @ApiOperation(value = "Refresh our list of games from EM")
    public ResponseEntity<Void> refreshEMGames() {
    	emRefreshService.scheduledResetGames();
    	emRefreshService.scheduledResetTable();
    	return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/game")
    @ApiOperation(value = "Get AllGames")
    public ResponseEntity<List<Game>> findAllGames() {
        LOGGER.info("Attempting to find all games");
        List<Game> games = gameService.findAllGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }
    
    @GetMapping("/game/{id}")
    @ApiOperation(value = "Get Game by id")
    public ResponseEntity<Game> findGame(@PathVariable Long id) {
        LOGGER.info("Attempting to find game with id {}", id);
        Game game = gameService.findGameById(id);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @GetMapping("/game/slug/{slug}")
    @ApiOperation(value = "Get Game by slug")
    public ResponseEntity<Game> findGame(@PathVariable String slug) {
        LOGGER.info("Attempting to find game with slug {}", slug);
        Game game = gameService.findGameBySlug(slug);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PutMapping("/game/{id}")
    @ApiOperation(value = "Update Game by id")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody UpdateGameRequest updateGameRequest) {
        LOGGER.info("Attempting to update game  {}", id);
        Game game = gameService.updateGame(id, updateGameRequest);
        return new ResponseEntity<>(game, HttpStatus.OK);
    }

    @PostMapping("/tag")
    @ApiOperation(value = "Create Tag")
    public ResponseEntity<Tag> createTag(@RequestBody TagRequest tagRequest) {
        LOGGER.info("Attempting to create tag  {}", tagRequest.toString());
        Tag tag = tagService.createTag(tagRequest);
        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @GetMapping("/tag")
    @ApiOperation(value = "Get All Tags")
    public ResponseEntity<List<Tag>> retrieveAllTag() {
        LOGGER.info("Attempting to get all tags");
        List<Tag> tags = tagService.retrieveAllTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
    
    @GetMapping("/tag/id/{id}")
    @ApiOperation(value = "Get Tag")
    public ResponseEntity<Tag> retrieveTag(@PathVariable Long id) {
        LOGGER.info("Attempting to get tag  {}", id.toString());
        Optional<Tag> tag = tagService.retrieveTag(id);
        return new ResponseEntity<>(tag.get(), HttpStatus.OK);
    }

    @PutMapping("/tag/id/{id}")
    @ApiOperation(value = "Update Tags by id")
    public ResponseEntity<Tag> updateTag(@PathVariable Long id, @RequestBody TagRequest tagRequest) {
        LOGGER.info("Attempting to update tag  {}", id.toString());
        Optional<Tag> tag = tagService.updateTag(id, tagRequest);
        return new ResponseEntity<>(tag.get(), HttpStatus.OK);
    }

    @DeleteMapping("/tag/id/{id}")
    @ApiOperation(value = "Delete Tags by id")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        LOGGER.info("Attempting to delete a tag  {}", id.toString());
        tagService.deleteTag(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
    
    // Game ordering
    @GetMapping("/ordering/{tag}/{country}/{terminal}")
    @ApiOperation(value = "Get All game orderings by tag and country")
    public ResponseEntity<List<GameOrdering>> retrieveGameOrderingByTagAndCountry(@PathVariable String tag, @PathVariable String country, @PathVariable String terminal) {
        LOGGER.info("Attempting to get all game orderings by tag {} and country {}", tag, country);
        List<GameOrdering> gameOrderings = gameOrderingService.getGameOrderingsByTagAndCountryAndTerminal(tag, country, terminal);
        return new ResponseEntity<>(gameOrderings, HttpStatus.OK);
    }
    
    @PutMapping("/ordering")
    @ApiOperation(value = "Update Tags by id")
    public ResponseEntity<List<GameOrdering>> updateTag(@RequestBody OrderingListRequest orderingRequest) {
        LOGGER.info("Attempting to update {} game orders", orderingRequest.getGameOrders().size());
        // Convert requests into entities
        List<GameOrdering> gos = new ArrayList<GameOrdering>();
        for (OrderingRequest o : orderingRequest.getGameOrders()) {
        	GameOrdering go = conversionService.convert(o, GameOrdering.class);
        	gos.add(go);
        }
        List<GameOrdering> updatedOrderings = gameOrderingService.updateGameOrderingsByTagAndCountryAndTerminal(gos);
        return new ResponseEntity<>(updatedOrderings, HttpStatus.OK);
    }
    
    
}
