package com.youbetcha.mocks;

import com.youbetcha.client.Login;
import com.youbetcha.model.ErrorData;
import com.youbetcha.model.ErrorDetails;
import com.youbetcha.model.dto.LoginDto;
import com.youbetcha.model.response.LoginPlayerResponse;
import com.youbetcha.model.response.LogoutPlayerResponse;
import com.youbetcha.model.response.PlayerCredentialValidationResponse;
import com.youbetcha.model.response.SessionIdValidationResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@Profile("ci")
public class MockLoginClient implements Login {

    private static final String TIMESTAMP = "2019-02-12 15:12:06";
    private static final String REGISTRATION_DATE = "/Date(1549287669963+0000)/";

    public Optional<LoginPlayerResponse> loginUser(LoginDto loginDto) {
        if (loginDto.getEmail().contains("fail")) {
            return Optional.of(createLoginPlayerFailureResponse());
        }
        return Optional.of(createLoginPlayerSuccessResponse());
    }

    public Optional<LogoutPlayerResponse> logout(String sessionId) {
        return Optional.of(createLogoutPlayerResponse());
    }

    @Override
    public Optional<PlayerCredentialValidationResponse> validateCredentials(String username, String password) {
        return Optional.of(createPlayerCredentialsValidationResponse());
    }

    @Override
    public Optional<SessionIdValidationResponse> validateSession(String sessionId) {
        return Optional.empty();
    }

    private LoginPlayerResponse createLoginPlayerSuccessResponse() {
        return LoginPlayerResponse.builder()
                //.errorData(generateSuccessfulLoginErrorData())
                .success((short) 1)
                .lastName("user")
                .firstName("test")
                .language("es")
                .version("1.0")
                .userId(1001001L)
                .registrationDate(REGISTRATION_DATE)
                .sessionId("0bf465bc-cc8d-4b85-80bc-6eafa1103dd2")
                .timestamp(TIMESTAMP)
                .build();
    }

    private LoginPlayerResponse createLoginPlayerFailureResponse() {
        return LoginPlayerResponse.builder()
                .errorData(generateUnSuccessfulLoginErrorData())
                .success((short) 0)
                .version("1.0")
                .timestamp(TIMESTAMP)
                .build();
    }

    private LogoutPlayerResponse createLogoutPlayerResponse() {
        return LogoutPlayerResponse.builder()
                .errorData(generateSuccessfulLoginErrorData())
                .success((short) 1)
                .timestamp(TIMESTAMP)
                .build();
    }

    private PlayerCredentialValidationResponse createPlayerCredentialsValidationResponse() {
        return PlayerCredentialValidationResponse.builder()
                .success((short) 1)
                .timestamp(TIMESTAMP)
                .build();
    }

    private ErrorData generateSuccessfulLoginErrorData() {
        return ErrorData.builder()
                .errorCode((short) 0)
                .errorDetails(Collections.emptyList())
                .errorMessage("")
                .logId(0)
                .build();
    }

    private ErrorData generateUnSuccessfulLoginErrorData() {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setErrorDetail("Some weird error");
        errorDetails.setId(105L);
        return ErrorData.builder()
                .errorCode((short) 1003)
                .errorDetails(Collections.singletonList(errorDetails))
                .logId(1)
                .build();
    }
}
