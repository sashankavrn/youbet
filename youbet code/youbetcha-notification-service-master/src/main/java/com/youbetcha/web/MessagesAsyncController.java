package com.youbetcha.web;

import com.google.gson.Gson;
import com.youbetcha.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class MessagesAsyncController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesAsyncController.class);

    @Autowired
    MessageService messageService;

    //using websockets
    @MessageMapping("/chat")
    @SendToUser("/topic/messages")
    public String send(@Payload String email) {
        LOGGER.info("Attempting to fetch users messages {}", email);
        return new Gson().toJson(messageService.findMessages(email, false));
    }

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
