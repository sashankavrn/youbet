package com.youbetcha.mocks;

import com.youbetcha.client.PreRegistration;
import com.youbetcha.model.response.RegistrationFieldAvailableResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.youbetcha.mocks.MockUserClient.generateErrorData;

@Component
@Profile("ci")
public class MockPreRegistrationClient implements PreRegistration {

    public Optional<RegistrationFieldAvailableResponse> validateUserNameAvailable(String userName) {
        return Optional.of(createRegistrationFieldAvailableSuccess());
    }

    public Optional<RegistrationFieldAvailableResponse> validateEmailAvailable(String userEmail) {
        return Optional.of(createRegistrationFieldAvailableSuccess());
    }

    public Optional<RegistrationFieldAvailableResponse> validateAliasAvailable(String alias) {
        return Optional.of(createRegistrationFieldAvailableSuccess());
    }

    private RegistrationFieldAvailableResponse createRegistrationFieldAvailableSuccess() {
        return RegistrationFieldAvailableResponse.builder()
                .errorData(generateErrorData())
                .success(1)
                .isAvailable(1)
                .version("ver1")
                .timestamp("2020-06-05")
                .build();
    }
}
