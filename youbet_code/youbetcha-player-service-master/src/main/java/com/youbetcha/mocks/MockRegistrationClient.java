package com.youbetcha.mocks;

import com.youbetcha.client.Registration;
import com.youbetcha.model.dto.PlayerReq;
import com.youbetcha.model.response.AssignUserRoleResponse;
import com.youbetcha.model.response.PlayerRegistrationResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.youbetcha.mocks.MockUserClient.generateErrorData;

@Component
@Profile("ci")
public class MockRegistrationClient implements Registration {

    private static final String TIMESTAMP = "2020-06-05: 12:00";

    public Optional<PlayerRegistrationResponse> registerUser(PlayerReq playerDto) {
        return Optional.of(createRegistrationResponse());
    }

    public Optional<PlayerRegistrationResponse> updateRegisteredUser(PlayerReq playerDto) {
        return Optional.of(createRegistrationResponse());
    }

    @Override
    public Optional<AssignUserRoleResponse> assignUserRole(String userID) {
        return Optional.of(assignUserRoleResponse());
    }

    private PlayerRegistrationResponse createRegistrationResponse() {
        return PlayerRegistrationResponse.builder()
                .errorData(generateErrorData())
                .success((short) 1)
                .userId(100L)
                .timestamp(TIMESTAMP)
                .build();
    }

    private AssignUserRoleResponse assignUserRoleResponse() {
        return AssignUserRoleResponse.builder()
                .errorData(generateErrorData())
                .success((short) 1)
                .timestamp(TIMESTAMP)
                .build();
    }
}
