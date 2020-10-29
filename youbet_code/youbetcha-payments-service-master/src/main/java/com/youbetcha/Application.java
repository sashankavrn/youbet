package com.youbetcha;

import com.youbetcha.kafka.PaymentsProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableBinding(PaymentsProcessor.class)
public class Application {
    public static void main(String[] args) {
        //set up logback file name
        if (System.getProperty("LOGS_FILENAME") != null) {
            System.setProperty("LOGS_FILENAME", "payments-service-logger");
        }
        SpringApplication.run(Application.class, args);
    }
}
