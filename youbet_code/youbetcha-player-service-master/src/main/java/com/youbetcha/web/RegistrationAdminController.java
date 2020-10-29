package com.youbetcha.web;

import com.youbetcha.exceptions.PlayerNotFoundException;
import com.youbetcha.model.dto.PlayerDto;
import com.youbetcha.model.dto.PlayerRegistrationResponseDto;
import com.youbetcha.service.RegistrationService;
import com.youbetcha.service.handler.ResponseHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/players")
@Api(tags = "Admin")
public class RegistrationAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationAdminController.class);

    @Autowired
    RegistrationService service;
    @Autowired
    ResponseHandler responseHandler;

    @ApiOperation(value = "Get all players")
    @GetMapping
    public ResponseEntity<List<PlayerDto>> getAllPlayers() {
        LOGGER.info("Attempting to get all players");
        List<PlayerDto> users = service.getPlayers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @ApiOperation(value = "Get player by id")
    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getById(@PathVariable long id) {
        LOGGER.info("Attempting to get player by id: {}", id);
        PlayerDto userById = service.getById(id);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @ApiOperation(value = "Get player by email")
    @GetMapping("/email/{email}")
    public ResponseEntity<PlayerDto> getByEmail(@PathVariable String email) {
        LOGGER.info("Attempting to get player by email: {}", email);
        PlayerDto user = service.getByEmail(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "Get failed response by User Id")
    @GetMapping("/response/{userId}")
    public ResponseEntity<List<PlayerRegistrationResponseDto>> getFailedResponseByUserId(@PathVariable String userName) {
        LOGGER.info("Attempting to get failed response by user id: {}", userName);
        Pageable pageable = PageRequest.of(0, 10);
        List<PlayerRegistrationResponseDto> responses = responseHandler.getFailedResponse(pageable, userName);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @ExceptionHandler({PlayerNotFoundException.class})
    public ResponseEntity<Object> handleException(PlayerNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}