package com.youbetcha.repository;

import com.youbetcha.model.entity.Game;
import com.youbetcha.model.entity.GameOrdering;
import com.youbetcha.model.games.Terminal;
import com.youbetcha.model.games.YBCategory;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DataJpaTest
@RunWith(SpringRunner.class)
@Sql("classpath:LoadGames.sql")
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    @Test @Ignore
    public void persistGameToDb() {
        Game game = TestDataHelper.createGame();
        gameRepository.save(game);

        Assert.assertNotNull(gameRepository.findById(game.getId()));
    }

    @Test @Ignore
    public void findAllGames() {
        List<Game> gamesList = gameRepository.findAll();
        Assert.assertNotNull(gamesList);
        assertTrue(gamesList.size() == 5);
    }

    @Test @Ignore
    public void findActiveGames() {
        Page<Game> gamesList = gameRepository.findActiveGames(TestDataHelper.createPageable());

        Assert.assertNotNull(gamesList);
    }

    @Test @Ignore
    public void findActiveGamesByCategory() {
        List<String> terminals = new ArrayList<>();
        terminals.add(Terminal.CROSS_PLATFORM.name());
        terminals.add(Terminal.MOBILE.name());
        Page<Game> gamesList = gameRepository.findActiveGamesByCountryAndCategoryAndTerminal(null, YBCategory.LIVE_CASINO.getYBCategoryValue(), terminals, TestDataHelper.createPageable());

        Assert.assertNotNull(gamesList);
        assertTrue(gamesList.getTotalElements() == 5);
    }

    @Test @Ignore
    public void findActiveGamesByNullCountryAndCategoryAndTag() {
        List<String> terminals = new ArrayList<>();
        terminals.add(Terminal.CROSS_PLATFORM.name());
        terminals.add(Terminal.MOBILE.name());
        Page<Game> gamesList = gameRepository.findActiveGamesByCountryAndCategoryAndTerminal(null, YBCategory.LIVE_CASINO.getYBCategoryValue(), terminals, TestDataHelper.createPageable());

        Assert.assertNotNull(gamesList);
        assertTrue(gamesList.getTotalElements() == 4);
    }

    @Test @Ignore
    public void findActiveGamesByCountryAndCategoryAndTag() {
        List<String> terminals = new ArrayList<>();
        terminals.add(Terminal.CROSS_PLATFORM.name());
        terminals.add(Terminal.MOBILE.name());
        Page<Game> gamesList = gameRepository.findActiveGamesByCountryAndCategoryAndTerminal("GB", YBCategory.LIVE_CASINO.getYBCategoryValue(), terminals, TestDataHelper.createPageable());

        Assert.assertNotNull(gamesList);
        assertTrue(gamesList.getTotalElements() == 4);
    }

    @Test @Ignore
    public void findGameById() {
        Optional<Game> game = gameRepository.findById(5l);
        Assert.assertNotNull(game.get());

//        List<GameOrdering> gameOverrideList = game.get().getGameOverrideList();
//        Assert.assertNotNull(gameOverrideList);
    }

    @Test @Ignore
    public void findGameBySlug() {
        Optional<Game> game = gameRepository.findBySlug("jackpot-roulette-ezugi");
        Assert.assertNotNull(game.get());

        assertEquals("jackpot-roulette-ezugi", game.get().getSlug());
    }

    @Test @Ignore
    public void findGameByIdWithNoOverrides() {
        Optional<Game> game = gameRepository.findById(5l);
        Assert.assertNotNull(game.get());

//        List<GameOrdering> gameOverrideList = game.get().getGameOverrideList();
//        Assert.assertTrue(gameOverrideList.size() == 1);
    }

    @Test @Ignore
    public void findDistinctTags() {
        List<String> tags = gameRepository.findDistinctTags(YBCategory.LIVE_CASINO.getYBCategoryValue());

        Assert.assertNotNull(tags);

    }
}
