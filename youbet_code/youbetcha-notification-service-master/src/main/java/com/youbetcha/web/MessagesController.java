package com.youbetcha.web;

import com.youbetcha.model.MessageDto;
import com.youbetcha.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/notifications")
@Api(tags = "Notifications")
public class MessagesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagesController.class);

    @Autowired
    MessageService messageService;

    @ApiOperation(value = "Update user's message as seen")
    @PutMapping("/{email}")
    public ResponseEntity<Void> updateMessage(
            @RequestBody MessageDto messageDto,
            @PathVariable String email) {
        LOGGER.info("Attempting to update user message status: {}", email);
        messageService.updateMessage(email, messageDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}