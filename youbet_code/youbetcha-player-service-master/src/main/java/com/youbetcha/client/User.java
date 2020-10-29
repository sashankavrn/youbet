package com.youbetcha.client;

import com.youbetcha.model.dto.ChangePasswordDto;
import com.youbetcha.model.dto.PlayerHashDto;
import com.youbetcha.model.response.ChangePasswordResponse;
import com.youbetcha.model.response.PlayerActivateResponse;
import com.youbetcha.model.response.PlayerHashResponse;
import com.youbetcha.model.response.UserDetailsResponse;

import java.util.Optional;

public interface User {

    Optional<PlayerHashResponse> generateUserHash(PlayerHashDto playerHashDto);

    Optional<PlayerActivateResponse> activateUser(String hashKey);

    Optional<UserDetailsResponse> userDetails(String sessionId);

    Optional<ChangePasswordResponse> changePassword(ChangePasswordDto passwordDto);
}
