package com.youbetcha.client;

import com.youbetcha.model.response.RegistrationFieldAvailableResponse;

import java.util.Optional;

public interface PreRegistration {

    Optional<RegistrationFieldAvailableResponse> validateUserNameAvailable(String userName);

    Optional<RegistrationFieldAvailableResponse> validateEmailAvailable(String userEmail);

    Optional<RegistrationFieldAvailableResponse> validateAliasAvailable(String alias);

}
