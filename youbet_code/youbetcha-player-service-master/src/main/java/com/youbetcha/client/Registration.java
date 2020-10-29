package com.youbetcha.client;

import com.youbetcha.model.dto.PlayerReq;
import com.youbetcha.model.dto.UserRoleReq;
import com.youbetcha.model.response.AssignUserRoleResponse;
import com.youbetcha.model.response.PlayerRegistrationResponse;

import java.util.Optional;

public interface Registration {

    Optional<PlayerRegistrationResponse> registerUser(PlayerReq playerDto);

    Optional<PlayerRegistrationResponse> updateRegisteredUser(PlayerReq playerDto);

    Optional<AssignUserRoleResponse> assignUserRole(String userID);

}
