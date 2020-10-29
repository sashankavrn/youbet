package com.youbetcha.web;

import com.youbetcha.exceptions.PlayerNotFoundException;
import com.youbetcha.model.dto.PlayerDto;
import com.youbetcha.model.dto.PlayerErrorDto;
import com.youbetcha.model.dto.PlayerRegistrationResponseDto;
import com.youbetcha.service.RegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/players")
@Api(tags = "Registration")
public class RegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    RegistrationService service;

    @ApiOperation(value = "Create player")
    @PostMapping
    public ResponseEntity<PlayerRegistrationResponseDto> createPlayer(@RequestBody PlayerDto user,
                                                                      @RequestHeader(name = "x-client-ip") String clientIp) {
        user.setSignupIp(clientIp);
        LOGGER.info("Attempting to create player: {}", user);

        Optional<PlayerRegistrationResponseDto> userResponse = service.add(user);

        List<PlayerErrorDto> errors = userResponse
                .map(PlayerRegistrationResponseDto::getPlayerErrorData)
                .orElse(new ArrayList<PlayerErrorDto>());

        if (!errors.isEmpty()) return userResponse
                .map(response -> new ResponseEntity<>(response, HttpStatus.BAD_REQUEST))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        userResponse.ifPresent(playerRegistrationResponseDto ->
                service.assignUserRole(playerRegistrationResponseDto.getUserId()));

        return userResponse
                .map(response -> new ResponseEntity<>(response, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @ApiOperation(value = "Update player")
    @PutMapping("/{id}")
    public ResponseEntity<PlayerRegistrationResponseDto> updatePlayer(@RequestBody PlayerDto dto,
                                                                      @PathVariable long id,
                                                                      @RequestHeader(name = "x-client-ip") String clientIp) {
        dto.setSignupIp(clientIp);
        LOGGER.info("Attempting to update player: {}", dto);
        return service.update(dto, id)
                .map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler({PlayerNotFoundException.class})
    public ResponseEntity<Object> handleException(PlayerNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
