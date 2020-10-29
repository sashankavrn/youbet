package com.youbetcha.mocks;

import com.youbetcha.client.User;
import com.youbetcha.model.ErrorData;
import com.youbetcha.model.dto.ChangePasswordDto;
import com.youbetcha.model.dto.PlayerHashDto;
import com.youbetcha.model.response.ChangePasswordResponse;
import com.youbetcha.model.response.PlayerActivateResponse;
import com.youbetcha.model.response.PlayerHashResponse;
import com.youbetcha.model.response.UserDetailsResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@Profile("ci")
public class MockUserClient implements User {

    private static final String TIMESTAMP = "2020-06-05";

    public static ErrorData generateErrorData() {
        return ErrorData.builder()
                .errorCode((short) 1)
                .errorDetails(Collections.emptyList())
                .logId(1)
                .build();
    }

    @Override
    public Optional<PlayerHashResponse> generateUserHash(PlayerHashDto playerHashDto) {
        return Optional.of(createPlayerHashResponse());
    }

    private PlayerHashResponse createPlayerHashResponse() {
        ErrorData errorData = generateErrorData();
        return PlayerHashResponse.builder()
                .errorData(errorData)
                .success((short) 1)
                .timestamp(TIMESTAMP)
                .URL("url")
                .version("1")
                .build();
    }

    @Override
    public Optional<PlayerActivateResponse> activateUser(String hashKey) {
        return Optional.of(createPlayerActivateResponse());
    }

    @Override
    public Optional<UserDetailsResponse> userDetails(String sessionId) {
        return Optional.of(createUserDetailsResponse());
    }

    @Override
    public Optional<ChangePasswordResponse> changePassword(ChangePasswordDto passwordDto) {
        return Optional.of(createPasswordResponse());
    }

    private ChangePasswordResponse createPasswordResponse() {
        return ChangePasswordResponse.builder()
                .errorData(generateErrorData())
                .session("123")
                .success((short) 1)
                .timestamp(TIMESTAMP).build();
    }

    private PlayerActivateResponse createPlayerActivateResponse() {
        ErrorData errorData = generateErrorData();
        return PlayerActivateResponse.builder()
                .errorData(errorData)
                .success((short) 1)
                .timestamp(TIMESTAMP)
                .userId(113L)
                .version("1")
                .build();
    }

    private UserDetailsResponse createUserDetailsResponse() {
        ErrorData errorData = generateErrorData();
        return UserDetailsResponse.builder()
                .errorData(errorData)
                .success((short) 1)
                .timestamp(TIMESTAMP)
                .version("1")
                .firstName("test")
                .lastName("user")
                .email("someEmail@test.com")
                .personalId("12345678")
                .build();

    }
}
