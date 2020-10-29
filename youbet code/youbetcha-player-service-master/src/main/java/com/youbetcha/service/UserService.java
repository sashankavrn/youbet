package com.youbetcha.service;

import com.youbetcha.client.User;
import com.youbetcha.exceptions.*;
import com.youbetcha.model.Player;
import com.youbetcha.model.dto.ChangePasswordDto;
import com.youbetcha.model.dto.PlayerHashDto;
import com.youbetcha.model.response.ChangePasswordResponse;
import com.youbetcha.model.response.PlayerActivateResponse;
import com.youbetcha.model.response.PlayerHashResponse;
import com.youbetcha.model.response.UserDetailsResponse;
import com.youbetcha.repository.PlayerRepository;
import com.youbetcha.service.handler.ApplicationEventPublisherHandler;
import com.youbetcha.service.handler.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    User userClient;
    @Autowired
    ResponseHandler responseHandler;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    ApplicationEventPublisherHandler eventPublisherHandler;

    public PlayerHashResponse createUserHash(PlayerHashDto playerHashDto) {
        PlayerHashResponse playerHashResponse = userClient.generateUserHash(playerHashDto)
                .orElseThrow(JsonNullOrEmptyException::new);
        validatePlayerHashResponse(playerHashDto, playerHashResponse);
        return playerHashResponse;
    }

    public PlayerActivateResponse activateUser(String hashKey) {
        PlayerActivateResponse playerActivateResponse = userClient.activateUser(hashKey)
                .orElseThrow(JsonNullOrEmptyException::new);
        validatePlayerActivateResponse(playerActivateResponse);
        return playerActivateResponse;
    }

    public Optional<UserDetailsResponse> userDetails(String sessionId) {
        return userClient.userDetails(sessionId);
    }

    public ChangePasswordResponse changePlayerPassword(String email, ChangePasswordDto dto) {
        return saveNewPassword(getUserByEmail(email), dto);
    }

    public Player getUserByEmail(String email) {
        return playerRepository.findByEmail(email).orElseThrow(() -> new PlayerNotFoundException(String.format("Player with email: %s not found", email)));
    }

    public Player updatePlayer(Player player) {
        return playerRepository.save(player);
    }

    private void validatePlayerHashResponse(PlayerHashDto dto, PlayerHashResponse response) {
        if (response.getSuccess() != 0) {
            LOGGER.info("Player with user id: {} hash generation successful", dto.getUserid());
        } else {
            LOGGER.warn("Player with user id: {} hash generation unsuccessful. Error Message: {}",
                    dto.getUserid(), response.getErrorData().getErrorMessage());
            throw new PlayerHashGenerationException(String.format("Player hash generation failed on user with user id %s", dto.getUserid()));
        }
    }

    private void validatePlayerActivateResponse(PlayerActivateResponse response) {
        if (response.getSuccess() != 0) {
            LOGGER.info("Player with user id: {} activation successful", response.getUserId());
        } else {
            LOGGER.error("Player with user id: {} activation unsuccessful. Error Message: {}",
                    response.getUserId(), response.getErrorData().getErrorMessage());
            throw new PlayerActivationException(String.format("Player activation failed on user with user id %s", response.getUserId()));
        }
    }

    public void resetPlayerPassword(String email) {
        eventPublisherHandler.constructResetPasswordEvent(getUserByEmail(email));
    }

    public void changePasswordWithResetToken(Player player, ChangePasswordDto changePasswordDto) {
        saveNewPassword(player, changePasswordDto);
    }

    private ChangePasswordResponse saveNewPassword(Player player, ChangePasswordDto dto) {
        player.setPassword(dto.getPlainTextPassword());
        playerRepository.save(player);
        Optional<ChangePasswordResponse> response = userClient.changePassword(dto);
        response.ifPresent(resp -> responseHandler.validateResponseCPSuccess(resp, player, dto));
        return response.orElseThrow(() ->
                new ChangePasswordFailureException(String.format(
                        "Password change failed for player with email %s and id %s", player.getEmail(), player.getId())));
    }
}
