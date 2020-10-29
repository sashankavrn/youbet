package com.youbetcha.mocks;

import com.youbetcha.client.Login;
import com.youbetcha.model.ErrorData;
import com.youbetcha.model.SessionIdValidationResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Profile("ci")
public class MockLoginClient implements Login {

    private static final String TIMESTAMP = "2019-02-12 15:12:06";
    private static final String REGISTRATION_DATE = "/Date(1549287669963+0000)/";

    @Override
    public Optional<SessionIdValidationResponse> validateSession(String sessionId) {
        return Optional.of(createSessionIdSuccessResponse());
    }

    private SessionIdValidationResponse createSessionIdSuccessResponse() {
        return SessionIdValidationResponse.builder()
                .errorData(generateSuccessfulLoginErrorData())
                .success((short) 1)
                .lastName("user")
                .firstName("test")
                .language("es")
                .version("1.0")
                .userId(1001001L)
                .timestamp(TIMESTAMP)
                .build();
    }

    private ErrorData generateSuccessfulLoginErrorData() {
        return ErrorData.builder()
                .errorCode((short) 0)
                .errorMessage("")
                .logId(0)
                .build();
    }
}
