package com.youbetcha.repository;

import com.youbetcha.model.entity.EMGame;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;

@DataJpaTest
@RunWith(SpringRunner.class)
@Sql("classpath:LoadEMGames.sql")
public class EMGameRepositoryTest {

    @Autowired
    private EMGameRepository EMGameRepository;

    @Test @Ignore
    public void persistGameToDb() {
        EMGame EMGame = TestDataHelper.createEMGame();
        EMGameRepository.save(EMGame);

        Assert.assertNotNull(EMGameRepository.findById(EMGame.getId()));
    }

    @Test @Ignore
    public void findAllGames() {
        List<EMGame> gamesList = EMGameRepository.findAll();
        Assert.assertNotNull(gamesList);
        assertTrue(gamesList.size() == 4);
    }

    @Test @Ignore
    public void findActiveGames() {
        List<EMGame> gamesList = EMGameRepository.findActiveGames();

        Assert.assertNotNull(gamesList);
    }

    @Test @Ignore
    public void findActiveGamesByCountry() {
        List<EMGame> gamesList = EMGameRepository.findActiveGamesByCountry("GB");

        Assert.assertNotNull(gamesList);
    }
}
