package com.youbetcha.web;

import com.youbetcha.exceptions.RegistrationFieldUnavailableException;
import com.youbetcha.model.response.RegistrationFieldAvailableResponse;
import com.youbetcha.service.PreRegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/preregistration")
@Api(tags = "Pre-Registration")
public class PreRegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreRegistrationController.class);

    @Autowired
    PreRegistrationService service;

    @GetMapping("/userNameAvailable/{userName}")
    @ApiOperation(value = "Check username availability")
    public ResponseEntity<RegistrationFieldAvailableResponse> userNameAvailable(@PathVariable String userName) {
        LOGGER.info("Attempting to check username availability");
    	RegistrationFieldAvailableResponse registrationFieldAvailableResponse = service.validateUserNameAvailable(userName);
    	return new ResponseEntity<>(registrationFieldAvailableResponse, HttpStatus.OK);
    }

    @GetMapping("/emailAddressAvailable/{emailAddress}")
    @ApiOperation(value = "Check email address availability")
    public ResponseEntity<RegistrationFieldAvailableResponse> emailAddressAvailable(@PathVariable String emailAddress) {
        LOGGER.info("Attempting to check email address availability");
        // This is to workaround a bug on the EM side
        // EM will accept a + in an email address (which is a valid character) for registration and login
        // However they don't accept it for checking if an email exists - it returns a 404 due to their IIS security policy
        // The best we can do is assume the email address is valid, as 99% of the time this will be used by us for testing
        if (emailAddress.contains("+")) {
        	LOGGER.warn("W> Email address check skipped for {} due to '+' character.", emailAddress);
        	RegistrationFieldAvailableResponse mockedResponse = RegistrationFieldAvailableResponse.builder()
        			.isAvailable(1)
        			.success(1)
        			.timestamp(LocalDateTime.now().toString())
        			.build();
        	return new ResponseEntity<>(mockedResponse, HttpStatus.OK);
        }
        RegistrationFieldAvailableResponse registrationFieldAvailableResponse = service.validateEmailAddressAvailable(emailAddress);
        return new ResponseEntity<>(registrationFieldAvailableResponse, HttpStatus.OK);
    }

    @ExceptionHandler({RegistrationFieldUnavailableException.class})
    public ResponseEntity<Object> handleException(RegistrationFieldUnavailableException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}