package com.youbetcha.service;

import com.youbetcha.config.SpringBootStartupHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EMRefreshService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootStartupHook.class);

    @Autowired
    GameService gameService;

    @Async
    //every 4 hours
    @Scheduled(cron = "0 0 */4 * * *", zone = "Europe/London")
    public void scheduledResetGames() {
        LOGGER.info("Refreshing games.....");
        //Game
        gameService.updateGames();
    }

    @Async
    //every hour reset the table data
    @Scheduled(cron = "0 0 */1 * * *", zone = "Europe/London")
    public void scheduledResetTable() {
        LOGGER.info("Refreshing tables.....");
        //Game
        gameService.updateTables();
    }
}
