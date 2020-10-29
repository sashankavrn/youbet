package com.youbetcha.service;

import com.google.common.base.Strings;
import com.youbetcha.client.LoginClient;
import com.youbetcha.client.Registration;
import com.youbetcha.exceptions.PlayerNotFoundException;
import com.youbetcha.model.Player;
import com.youbetcha.model.dto.*;
import com.youbetcha.model.response.AssignUserRoleResponse;
import com.youbetcha.model.response.PlayerRegistrationResponse;
import com.youbetcha.repository.PlayerRepository;
import com.youbetcha.service.handler.ResponseHandler;
import com.youbetcha.util.PlayerDtoValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.kafka.KafkaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationService.class);

    @Autowired
    PlayerRepository repository;
    @Autowired
    Registration registrationClient;
    @Autowired
    ResponseHandler responseHandler;
    @Qualifier(value = "mvcConversionService")
    @Autowired
    ConversionService converter;

    @Transactional(noRollbackFor = KafkaException.class)
    public Optional<PlayerRegistrationResponseDto> add(PlayerDto dto) {
        List<PlayerErrorDto> errors = PlayerDtoValidator.validate(dto);

        if (!errors.isEmpty()) {
            PlayerRegistrationResponseDto errorResponse = new PlayerRegistrationResponseDto();
            errorResponse.setPlayerErrorData(errors);
            return Optional.of(errorResponse);
        }
        
        Player player = converter.convert(dto, Player.class);
        player.setDepositCount(0);
        player.setWithdrawCount(0);
        persistUserToDb(dto, player);
        setUserNameToUserId(dto, player);
        PlayerReq convert = converter.convert(dto, PlayerReq.class);
        Optional<PlayerRegistrationResponse> registrationResponse = registrationClient.registerUser(convert);
        registrationResponse.ifPresent(resp -> responseHandler.validateResponseSuccess(resp, player));
        return registrationResponse.map(x -> converter.convert(x, PlayerRegistrationResponseDto.class));
    }

    @Transactional(noRollbackFor = KafkaException.class)
    public Optional<PlayerRegistrationResponseDto> update(PlayerDto updatedUser, long id) {
        Player player = repository.findById(id).orElseThrow(() -> new PlayerNotFoundException("Couldn't find a Player with id: " + id));
        PlayerDto convert = converter.convert(player, PlayerDto.class);
        updatedUser.setId(convert.getId());
        Optional<PlayerRegistrationResponse> response = registrationClient.updateRegisteredUser(
                converter.convert(updatedUser, PlayerReq.class));
        return response.map(x -> converter.convert(x, PlayerRegistrationResponseDto.class));
    }

    public List<PlayerDto> getPlayers() {
        List<Player> playerList = (List<Player>) repository.findAll();
        LOGGER.info("Fetched {} players from database", playerList.size());
        return playerList.stream().map(x -> converter.convert(x, PlayerDto.class)).collect(Collectors.toList());
    }


    public PlayerDto getById(long id) {
        return repository.findById(id).map(player -> converter.convert(player, PlayerDto.class))
                .orElseThrow(() -> new PlayerNotFoundException("Couldn't find a Player with id: " + id));
    }

    public PlayerDto getByEmail(String email) {
        return repository.findByEmail(email).map(player -> converter.convert(player, PlayerDto.class))
                .orElseThrow(() -> new PlayerNotFoundException("Couldn't find a Player with email: " + email));
    }

    private void setUserNameToUserId(PlayerDto dto, Player player) {
        dto.setUserName(Strings.padStart(String.valueOf(player.getId()), 4, '0'));
        dto.setTitle("Mr.");
    }

    private void persistUserToDb(PlayerDto dto, Player player) {
    	// Store the password as a hash
    	player.setPassword(LoginClient.hashPassword(dto.getPassword()));
        repository.save(Objects.requireNonNull(player));
        LOGGER.info("Saved to the db {}", dto.getEmail());
    }

    public void assignUserRole(Long userID) {
        Optional<AssignUserRoleResponse> roleResponse = registrationClient.assignUserRole(userID.toString());

        if (!roleResponse.isPresent()) {
            LOGGER.warn("Failed to assign a role to the user {}", userID);
        } else {
            LOGGER.info("Success assigning role to the user {}", userID);
        }
    }
}
