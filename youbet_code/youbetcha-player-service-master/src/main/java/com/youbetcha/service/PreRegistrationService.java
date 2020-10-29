package com.youbetcha.service;

import com.youbetcha.client.PreRegistration;
import com.youbetcha.exceptions.RegistrationFieldUnavailableException;
import com.youbetcha.model.response.RegistrationFieldAvailableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PreRegistrationService {

    public static final String UNAVAILABLE = " unavailable.";
    @Autowired
    PreRegistration preRegistrationClient;

    public RegistrationFieldAvailableResponse validateUserNameAvailable(String userName) {
        return preRegistrationClient.validateUserNameAvailable(userName)
                .orElseThrow(() -> new RegistrationFieldUnavailableException("Registration field username " + userName + UNAVAILABLE));
    }

    public RegistrationFieldAvailableResponse validateEmailAddressAvailable(String emailAddress) {
        return preRegistrationClient.validateEmailAvailable(emailAddress)
                .orElseThrow(() -> new RegistrationFieldUnavailableException("Registration field email address " + emailAddress + UNAVAILABLE));
    }

    public RegistrationFieldAvailableResponse validateAliasAvailable(String alias) {
        return preRegistrationClient.validateAliasAvailable(alias)
                .orElseThrow(() -> new RegistrationFieldUnavailableException("Registration field alias " + alias + UNAVAILABLE));
    }
}