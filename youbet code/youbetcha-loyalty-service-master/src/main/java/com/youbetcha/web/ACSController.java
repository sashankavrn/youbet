package com.youbetcha.web;

import com.youbetcha.model.AcsAuthenticateResponse;
import com.youbetcha.service.AcsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bonus")
@Api(tags = "Bonus")
public class ACSController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ACSController.class);

    @Autowired
    AcsService service;

    @PostMapping("/acs")
    @ApiOperation(value = "Authenticate")
    public ResponseEntity<AcsAuthenticateResponse> authenticate() {
        LOGGER.info("Trying to authenticate");
        return new ResponseEntity<>(service.authenticate(), HttpStatus.OK);
    }
}
