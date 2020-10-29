package com.youbetcha.web;

import com.youbetcha.exceptions.PlayerNotFoundException;
import com.youbetcha.model.dto.ChangePasswordDto;
import com.youbetcha.service.UserService;
import com.youbetcha.service.VerificationService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/verification")
@Api(tags = "Player Verification")
public class VerificationController {

    public static final String VALID = "valid";
    public static final String TOKEN_S = "Token %s";
    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationController.class);
    @Autowired
    VerificationService verificationService;
    @Autowired
    UserService userService;

    @PutMapping("/registrationConfirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") final String token) {
        LOGGER.info("Attempting to confirm registration for token: {}", token);
        final String result = verificationService.validateVerificationToken(token);
        return result.equals(VALID) ?
                new ResponseEntity<>("Token valid - Registration Confirmed", HttpStatus.OK) :
                new ResponseEntity<>(String.format(TOKEN_S, result), HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam("token") final String token) {
        LOGGER.info("Attempting to reset password for token: {}", token);
        final String result = verificationService.validateVerificationToken(token);
        return result.equals(VALID) ?
                new ResponseEntity<>("Token valid - Reset Password", HttpStatus.OK) :
                new ResponseEntity<>(String.format(TOKEN_S, result), HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/saveNewPassword")
    public ResponseEntity<String> saveNewPassword(@RequestBody ChangePasswordDto changePasswordDto) {
        String token = changePasswordDto.getOldPlainTextPassword();
        LOGGER.info("Attempting to save new password password for token: {}", token);
        final String result = verificationService.validateVerificationToken(token);
        if (result.equals(VALID)) {
            userService.changePasswordWithResetToken(verificationService.getPlayer(token), changePasswordDto);
            return new ResponseEntity<>("Token valid - Reset Password", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(String.format(TOKEN_S, result), HttpStatus.UNAUTHORIZED);
        }
    }

    @ExceptionHandler({PlayerNotFoundException.class})
    public ResponseEntity<Object> handleException(PlayerNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}