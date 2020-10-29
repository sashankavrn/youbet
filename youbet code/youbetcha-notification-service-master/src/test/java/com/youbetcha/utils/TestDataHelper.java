package com.youbetcha.utils;

import com.youbetcha.model.MailRequest;
import com.youbetcha.model.MailResponse;
import com.youbetcha.model.Message;

import java.sql.Timestamp;
import java.time.Instant;

public class TestDataHelper {

    public static MailRequest createMailRequest() {
        return MailRequest.builder()
                .firstName("Test")
                .lastName("user")
                .emailAddress("newreg@test.account")
                .build();
    }

    public static MailResponse createMailResponse() {
        MailResponse mailResponse = new MailResponse();
        mailResponse.setMessage("Email has been sent");
        return mailResponse;
    }

    public static Message createMessage() {
        Message message = new Message();
        message.setUserId(3L);
        message.setSeen(false);
        message.setCreatedDate(Timestamp.from(Instant.now()));
        message.setLastModifiedDate(null);
        message.setContent("Welcome to Youbetcha");
        return message;
    }
}
