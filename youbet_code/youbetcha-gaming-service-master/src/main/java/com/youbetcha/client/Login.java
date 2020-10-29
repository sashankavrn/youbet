package com.youbetcha.client;

import com.youbetcha.model.SessionIdValidationResponse;

import java.util.Optional;

public interface Login {
    Optional<SessionIdValidationResponse> validateSession(String sessionId);
}
