package com.youbetcha.repository;

import com.youbetcha.model.Player;
import com.youbetcha.utils.TestDataHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

@DataJpaTest
@RunWith(SpringRunner.class)
public class RegistrationIntegrationTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    public void persistUserToDb() {
        Player player = TestDataHelper.createUser();
        player.setCreatedDate(Date.from(Instant.now()));
        player.setLastModifiedDate(Date.from(Instant.now()));
        playerRepository.save(player);

        Assert.assertNotNull(playerRepository.findById(player.getId()));
    }

    @Test
    public void deleteUserInDb() {
        Player player = TestDataHelper.createUser();
        player.setCreatedDate(Date.from(Instant.now()));
        player.setLastModifiedDate(Date.from(Instant.now()));
        playerRepository.save(player);
        playerRepository.deleteById(player.getId());

        Optional<Player> byId = playerRepository.findById(player.getId());
        Assert.assertFalse(byId.isPresent());
    }
}
