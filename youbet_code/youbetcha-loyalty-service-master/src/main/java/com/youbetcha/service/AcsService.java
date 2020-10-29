package com.youbetcha.service;

import com.youbetcha.client.AcsClient;
import com.youbetcha.model.AcsAuthenticateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcsService {

    @Autowired
    AcsClient acsClient;

    public AcsAuthenticateResponse authenticate() {
        return acsClient.authenticate()
                .orElseThrow(() -> new RuntimeException("Authenticate error"));
    }
}
