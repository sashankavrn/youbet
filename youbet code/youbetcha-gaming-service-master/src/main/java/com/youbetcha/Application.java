package com.youbetcha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class Application {
    public static void main(String[] args) {
        //set up logback file name
        if (System.getProperty("LOGS_FILENAME") != null) {
            System.setProperty("LOGS_FILENAME", "gaming-service-logger");
        }
        SpringApplication.run(Application.class, args);
    }
}
