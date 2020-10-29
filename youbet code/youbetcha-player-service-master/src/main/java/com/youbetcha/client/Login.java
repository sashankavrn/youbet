package com.youbetcha.client;

import com.youbetcha.model.dto.LoginDto;
import com.youbetcha.model.response.LoginPlayerResponse;
import com.youbetcha.model.response.LogoutPlayerResponse;
import com.youbetcha.model.response.PlayerCredentialValidationResponse;
import com.youbetcha.model.response.SessionIdValidationResponse;

import java.util.Optional;

public interface Login {
    Optional<LoginPlayerResponse> loginUser(LoginDto loginDto);

    Optional<LogoutPlayerResponse> logout(String sessionId);

    Optional<PlayerCredentialValidationResponse> validateCredentials(String username, String password);

    Optional<SessionIdValidationResponse> validateSession(String sessionId);
}
