package com.youbetcha;

import com.youbetcha.kafka.NotificationsMessageProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

@SpringBootApplication
@EnableBinding(NotificationsMessageProcessor.class)
public class Application {
    public static void main(String[] args) {
        //set up logback file name
        if (System.getProperty("LOGS_FILENAME") != null) {
            System.setProperty("LOGS_FILENAME", "player-notification-logger");
        }
        SpringApplication.run(Application.class, args);
    }
}
