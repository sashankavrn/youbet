package com.youbetcha.service;

import com.youbetcha.model.MessageDto;
import com.youbetcha.model.PushNotification;
import com.youbetcha.repository.PushNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    PushNotificationRepository repository;

    public void updateMessage(String email, MessageDto messageDto) {
        //get pushNotification
        PushNotification pushNotification = repository.findByContentAndEmailAddress(messageDto.getMessage(), email).stream().findAny().orElseThrow(() -> new RuntimeException(""));
        //update pushNotification
        pushNotification.setSeen(true);
        repository.save(pushNotification);
    }

    public List<PushNotification> findMessages(String email, boolean seen) {
        return repository.findByEmailAddressAndSeen(email, seen);
    }

    public void createPushNotification(PushNotification pushNotification) {
        repository.save(pushNotification);
    }
}
