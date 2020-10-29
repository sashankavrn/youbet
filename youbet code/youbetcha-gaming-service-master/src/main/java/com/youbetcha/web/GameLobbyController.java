package com.youbetcha.web;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.youbetcha.jwt.JwtTokenUtil;
import com.youbetcha.model.games.GameCategoryResponse;
import com.youbetcha.model.games.GameDto;
import com.youbetcha.model.games.GameTagResponse;
import com.youbetcha.model.games.TagDto;
import com.youbetcha.model.games.Terminal;
import com.youbetcha.model.games.YBCategory;
import com.youbetcha.service.GameLobbyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;


@RestController
@RequestMapping("/api/v1/games/lobby")
@Api(tags = "Games")
public class GameLobbyController {

    private static final String DEFAULT_COUNTRY_CODE = "XX";

	private static final Logger LOGGER = LoggerFactory.getLogger(GameLobbyController.class);

    @Autowired
    GameLobbyService gameLobbyService;

    @Autowired
    ConversionService conversionService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping(path = {"/category/{category}", "/category/{category}/terminal/{terminal}"})
    @ApiOperation(value = "Get Games for a given category")
    public ResponseEntity<GameCategoryResponse> getCategoryData(
            @ApiParam(name = "terminal", value = "User Device Type", example = "MOBILE")
            @PathVariable(name = "terminal", required = false) Optional<Terminal> terminal,
            @ApiParam(name = "category", value = "Filter by category")
            @PathVariable(name = "category") String category,
            @ApiParam(name = "page", value = "Page number", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(name = "size", value = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @CurrentSecurityContext(expression = "authentication.principal") Principal principal) {

        Pageable pageable = PageRequest.of(page, size);
        
        Terminal activeTerminal = Terminal.MOBILE;
        if (terminal != null && terminal.isPresent()) {
        	activeTerminal = terminal.get();
        }
        
        String countryCode = null;
        if (principal != null && principal.getName().contains(".")) {
            countryCode = jwtTokenUtil.getCountryFromToken(principal.getName());
        }
        if (countryCode == null || countryCode.isEmpty()) {
        	countryCode = DEFAULT_COUNTRY_CODE;
        }
        LOGGER.info("Attempting to fetch games for category {} at country {} and device {}", category, countryCode, activeTerminal.name());

        // Given a category, we want to return all games, with the given order for the country (if it exists, otherwise default)
        Map<TagDto, List<GameDto>> allGames = gameLobbyService.getGamesByCategory(countryCode, YBCategory.getYBCategoryByValue(category), activeTerminal, pageable);
        
//        LOGGER.debug("D> Sending back {} tags for category {} and country {}", allGames.size(), category, countryCode);
        GameCategoryResponse gameLobbyResponse = createCategoryResponse(allGames);
        //now convert to game dto
        return new ResponseEntity<>(gameLobbyResponse, HttpStatus.OK);
    }
    
    private GameCategoryResponse createCategoryResponse(Map<TagDto, List<GameDto>> allGames) {
        return GameCategoryResponse.builder()
                .games(allGames)
                .build();
    }

    @GetMapping(path = {"/tag/{tag}", "/tag/{tag}/terminal/{terminal}"})
    @ApiOperation(value = "Get Games for a given tag")
    public ResponseEntity<GameTagResponse> getTagData(
            @ApiParam(name = "terminal", value = "User Device Type", example = "MOBILE and DESKTOP")
            @PathVariable(name = "terminal", required = false) Optional<Terminal> terminal,
            @ApiParam(name = "page", value = "Page number", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @ApiParam(name = "size", value = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size,
            @ApiParam(name = "tag", value = "Filter by tag")
            @PathVariable(name = "tag") String tag,
            @CurrentSecurityContext(expression = "authentication.principal") Principal principal) {

        Terminal activeTerminal = Terminal.MOBILE;
        if (terminal != null && terminal.isPresent()) {
        	activeTerminal = terminal.get();
        }
        
        String countryCode = null;
        if (principal != null && principal.getName().contains(".")) {
            countryCode = jwtTokenUtil.getCountryFromToken(principal.getName());
        }
        if (countryCode == null || countryCode.isEmpty()) {
        	countryCode = DEFAULT_COUNTRY_CODE;
        }
        LOGGER.info("Attempting to fetch games for tag  {} at country {} and device {}", tag, countryCode, activeTerminal.name());

    	
        Pageable pageable = PageRequest.of(page, size);

        // Given a tag, we want to return all games, with the given order for the country (if it exists, otherwise default)
        List<GameDto> gameDtos = gameLobbyService.getGamesByTag(countryCode, tag, activeTerminal, pageable);
        
        GameTagResponse gameTagResponse = createTagResponse(gameDtos);
        return new ResponseEntity<>(gameTagResponse, HttpStatus.OK);
    }

    private GameTagResponse createTagResponse(List<GameDto> allGames) {
        return GameTagResponse.builder()
                .games(allGames)
                .build();
    }

}
