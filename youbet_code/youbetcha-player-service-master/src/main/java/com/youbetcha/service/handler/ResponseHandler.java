package com.youbetcha.service.handler;

import com.google.gson.Gson;
import com.youbetcha.kafka.PlayerMessageHandler;
import com.youbetcha.model.Player;
import com.youbetcha.model.dto.ChangePasswordDto;
import com.youbetcha.model.dto.PlayerRegistrationResponseDto;
import com.youbetcha.model.event.PushNotification;
import com.youbetcha.model.response.ChangePasswordResponse;
import com.youbetcha.model.response.PlayerRegistrationResponse;
import com.youbetcha.repository.PlayerRepository;
import com.youbetcha.repository.RegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResponseHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseHandler.class);

    @Autowired
    PlayerRepository repository;
    @Autowired
    RegistrationRepository responseRepository;
    @Autowired
    ApplicationEventPublisherHandler eventPublisherHandler;
    @Qualifier(value = "mvcConversionService")
    @Autowired
    ConversionService converter;
    Gson gson = new Gson();

    @Autowired
    private PlayerMessageHandler playerMessageHandler;

    public void validateResponseSuccess(PlayerRegistrationResponse resp, Player player) {
        if (resp.getSuccess() != 0) {
            LOGGER.info("Player: {} registration successful", resp.getUserId());
            assignUserIdAndPersist(resp, player);
            eventPublisherHandler.constructConfirmRegistrationEvent(player);
            playerMessageHandler.pushNotification(PushNotification.builder().inApp(true).emailAddress(player.getEmail()).content("Welcome to Youbetcha. Let's play").build());
        } else {
            LOGGER.error("Player registration unsuccessful. Message: {}", resp.getErrorData().getErrorMessage());
            responseRepository.insertFailedResponse(player.getEmail(), gson.toJson(resp));
        }
    }

    public List<PlayerRegistrationResponseDto> getFailedResponse(Pageable pageable, String userName) {
        Page<String> responses = responseRepository.findAllFailedResponsePagination(pageable, userName);
        return responses.stream()
                .map(resp -> converter.convert(gson.fromJson(resp, PlayerRegistrationResponse.class), PlayerRegistrationResponseDto.class))
                .collect(Collectors.toList());
    }

    public void validateResponseCPSuccess(ChangePasswordResponse resp, Player player, ChangePasswordDto dto) {
        if (resp.getSuccess() != 0) {
            LOGGER.info("Successful password change for player with email {} and id {}", player.getEmail(), player.getId());
            playerMessageHandler.pushNotification(PushNotification.builder().inApp(true).emailAddress(player.getEmail()).content("Password has been successfully changed").build());
            eventPublisherHandler.constructPasswordChangeSuccessEvent(player);
        } else {
            LOGGER.error("Unsuccessful password change for player: {}", resp.getErrorData().getErrorMessage());
            player.setPassword(dto.getOldPlainTextPassword());
            repository.save(player);
            responseRepository.insertFailedResponse(player.getEmail(), gson.toJson(resp));
        }
    }

    private void assignUserIdAndPersist(PlayerRegistrationResponse resp, Player player) {
        player.setEverymatrixUserId(resp.getUserId());
        repository.save(player);
    }
}
