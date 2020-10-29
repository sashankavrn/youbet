package com.youbetcha.web;

import com.youbetcha.exceptions.JsonNullOrEmptyException;
import com.youbetcha.exceptions.PlayerActivationException;
import com.youbetcha.exceptions.PlayerHashGenerationException;
import com.youbetcha.exceptions.PlayerNotFoundException;
import com.youbetcha.model.dto.ChangePasswordDto;
import com.youbetcha.model.dto.PlayerHashDto;
import com.youbetcha.model.dto.UserDetailsDto;
import com.youbetcha.model.response.ChangePasswordResponse;
import com.youbetcha.model.response.PlayerActivateResponse;
import com.youbetcha.model.response.PlayerHashResponse;
import com.youbetcha.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@Api(tags = "Players")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    @Qualifier(value = "mvcConversionService")
    @Autowired
    ConversionService converter;

    @ApiOperation(value = "Generate Hash for user to use")
    @PostMapping("/hash")
    public ResponseEntity<PlayerHashResponse> generateUserHash(@RequestBody PlayerHashDto playerHashDto) {
        LOGGER.info("Attempting to generate user hash");
        PlayerHashResponse response = userService.createUserHash(playerHashDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Activate player")
    @GetMapping("/activatePlayer/{hashKey}")
    public ResponseEntity<PlayerActivateResponse> activatePlayer(@PathVariable String hashKey) {
        LOGGER.info("Attempting to activate player");
        PlayerActivateResponse response = userService.activateUser(hashKey);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Get Users Details")
    @GetMapping("/playerDetails")
    public ResponseEntity<UserDetailsDto> getPlayerDetails(@CurrentSecurityContext(expression = "authentication.principal") Principal principal) {
        LOGGER.info("Attempting to fetch user details with session id: {} can deposit.", principal.getName());
        UserDetailsDto response = userService.userDetails(principal.getName())
                .map(u -> converter.convert(u, UserDetailsDto.class))
                .orElseThrow(JsonNullOrEmptyException::new);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Change player password")
    @PostMapping("/password/change/{email}")
    public ResponseEntity<ChangePasswordResponse> changePlayerPassword(@PathVariable String email,
                                                                       @RequestBody ChangePasswordDto dto) {
        LOGGER.info("Attempting to change player password");
        ChangePasswordResponse changePasswordResponse = userService.changePlayerPassword(email, dto);
        return new ResponseEntity<>(changePasswordResponse, HttpStatus.OK);
    }

    @ApiOperation(value = "Reset player password")
    @PostMapping("/password/reset")
    public ResponseEntity<ChangePasswordResponse> resetPlayerPassword(@RequestParam String email) {
        LOGGER.info("Attempting to reset player password");
        userService.resetPlayerPassword(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler({PlayerActivationException.class})
    public ResponseEntity<Object> handleException(PlayerActivationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({PlayerHashGenerationException.class})
    public ResponseEntity<Object> handleException(PlayerHashGenerationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({PlayerNotFoundException.class})
    public ResponseEntity<Object> handleException(PlayerNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
