package com.youbetcha.web;

import com.youbetcha.model.dto.LoginDto;
import com.youbetcha.model.response.LogoutPlayerResponse;
import com.youbetcha.model.response.PlayerCredentialValidationResponse;
import com.youbetcha.service.Authentication;
import com.youbetcha.service.LoginService;
import com.youbetcha.service.VerificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/login")
@Api(tags = "Login")
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    LoginService service;
    @Autowired
    VerificationService verificationService;
    @Autowired
    Authentication authenticationService;

    @PostMapping()
    @ApiOperation(value = "Log in a player")
    public ResponseEntity<?> loginPlayer(@RequestBody LoginDto loginDto,
                                         @RequestHeader(name = "x-client-ip") String clientIp,
                                         HttpServletResponse response) {
        loginDto.setIp(clientIp);
        LOGGER.info("Attempting to login user {}", loginDto);
        if (verificationService.isUserEnabled(loginDto)) {
            return service.loginUser(loginDto)
                    .map(responseDto -> {
                        if (responseDto.getSuccess() == LoginService.UN_SUCCESSFUL_LOGIN) {
                            LOGGER.error("Error logging in {}", responseDto.toString());
                            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                        }
                        LOGGER.info("UserResponseDto={} ", responseDto.toString());
                        String authenticate = authenticationService.authenticate(responseDto.getSessionId(), loginDto.getEmail());
                        HttpHeaders respHeaders = new HttpHeaders();
                        respHeaders.set("jwt", authenticate);
                        Cookie cookie = new Cookie("youbetcha", authenticate);
                        cookie.setDomain("youbetcha.com");
                        response.addCookie(cookie);
                        return new ResponseEntity<>(null, respHeaders, HttpStatus.OK);
                    })
                    .orElse(new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
        } else {
            LOGGER.info("User account with email: {} is disabled", loginDto.getEmail());
            return new ResponseEntity<>("User account is disabled", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/validate")
    @ApiOperation(value = "Validate player credentials")
    public ResponseEntity<PlayerCredentialValidationResponse> validatePlayerCredentials(LoginDto loginDto,
                                                                                        @RequestHeader(name = "x-client-ip") String clientIp) {
        loginDto.setIp(clientIp);
        LOGGER.info("Attempting to validate player credentials: {}", loginDto);
        PlayerCredentialValidationResponse response = service.validateUserCredentials(loginDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/logout/{sessionId}")
    @ApiOperation(value = "Log out a player")
    public ResponseEntity<LogoutPlayerResponse> logout(@PathVariable String sessionId) {
        LOGGER.info("Attempting to logout player from session: {}", sessionId);
        LogoutPlayerResponse logout = service.logout(sessionId);
        return new ResponseEntity<>(logout, HttpStatus.OK);
    }
}
