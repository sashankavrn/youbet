package com.youbetcha.config;

import com.youbetcha.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringBootStartupHook {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootStartupHook.class);

    @Autowired
    GameService gameService;

    @Bean
    CommandLineRunner runner() {
        return args -> {
            LOGGER.info("Server has started.... syncing games to the database");
            try {
                gameService.updateGames();
                gameService.updateTables();
            } catch (Exception e) {
                LOGGER.error("Startup error ", e);
            }
        };
    }


}
