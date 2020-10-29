package com.youbetcha.service;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.youbetcha.client.Login;
import com.youbetcha.exceptions.JsonNullOrEmptyException;
import com.youbetcha.exceptions.UnsuccessfulPlayerLogoutException;
import com.youbetcha.kafka.PlayerMessageHandler;
import com.youbetcha.model.LoginEntity;
import com.youbetcha.model.dto.LoginDto;
import com.youbetcha.model.event.PushNotification;
import com.youbetcha.model.response.LoginPlayerResponse;
import com.youbetcha.model.response.LogoutPlayerResponse;
import com.youbetcha.model.response.PlayerCredentialValidationResponse;
import com.youbetcha.repository.LoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LoginService {

    public static final int UN_SUCCESSFUL_LOGIN = 0;
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
    @Autowired
    LoginRepository loginRepository;
    @Qualifier(value = "mvcConversionService")
    @Autowired
    ConversionService converter;
    @Autowired
    UserService userService;
    @Autowired
    Login loginClient;
    Gson gson = new Gson();

    @Autowired
    private PlayerMessageHandler playerMessageHandler;

    @Transactional
    public Optional<LoginPlayerResponse> loginUser(LoginDto loginDto) {
        Long id = userService.getUserByEmail(loginDto.getEmail()).getId();
        String userName = Strings.padStart(String.valueOf(id), 4, '0');
        loginDto.setUserName(userName);
        Optional<LoginPlayerResponse> loginPlayerResponse = loginClient.loginUser(
                loginDto);
        loginPlayerResponse.ifPresent(r -> validateResponseSuccess(loginDto.getEmail(), r));
        return loginPlayerResponse;
    }

    public LogoutPlayerResponse logout(String sessionId) {
        LogoutPlayerResponse logoutPlayerResponse = loginClient.logout(sessionId).orElseThrow(JsonNullOrEmptyException::new);
        validateLogoutPlayerResponse(sessionId, logoutPlayerResponse);
        return logoutPlayerResponse;
    }

    private void validateResponseSuccess(String email, LoginPlayerResponse resp) {
        if (resp.getSuccess() != UN_SUCCESSFUL_LOGIN) {
            LOGGER.info("Player with user id: {} successfully logged in.", resp.getUserId());
            LoginEntity login = converter.convert(resp, LoginEntity.class);
            loginRepository.save(login);
            playerMessageHandler.pushNotification(PushNotification.builder().emailAddress(email).inApp(true).content("Welcome back. Try out our newest games").build());
        } else {
            LOGGER.warn("Player with email: {} failed to login, with error message: {}.",
                    email, resp.getErrorData().getErrorMessage());
            insertFailedLoginResponse(email, gson.toJson(resp));
        }
    }

    public Page<String> getFailedResponse(Pageable pageable, String userName) {
        Page<String> responses = loginRepository.findAllFailedResponsePagination(pageable, userName);
        return responses;
    }

    public PlayerCredentialValidationResponse validateUserCredentials(LoginDto loginDto) {
        PlayerCredentialValidationResponse response = loginClient
                .validateCredentials(loginDto.getEmail(), loginDto.getPassword())
                .orElseThrow(JsonNullOrEmptyException::new);
        validatePlayerCredentialValidationResponse(response);
        return response;
    }

    private void validatePlayerCredentialValidationResponse(PlayerCredentialValidationResponse validationResponse) {
        if (validationResponse.getSuccess() != UN_SUCCESSFUL_LOGIN) {
            LOGGER.info("Player credentials validated successfully with valid: {}", validationResponse.getIsValid());
        } else {
            LOGGER.warn("Player credentials validated unsuccessfully with error message: {}."
                    , validationResponse.getErrorData().getErrorMessage());
        }
    }

    private void validateLogoutPlayerResponse(String sessionId, LogoutPlayerResponse logoutPlayerResponse) {
        if (logoutPlayerResponse.getSuccess() != UN_SUCCESSFUL_LOGIN) {
            LOGGER.info("Player logged out successfully from session: {}", sessionId);
        } else {
            LOGGER.error("Player logged out unsuccessfully from session: {} with error message: {}."
                    , sessionId, logoutPlayerResponse.getErrorData().getErrorMessage());
            throw new UnsuccessfulPlayerLogoutException("Player logged out unsuccessfully from session");
        }
    }

    public void insertFailedLoginResponse(String email, String responseString) {
        loginRepository.insertFailedResponse(email, responseString);
    }
}
