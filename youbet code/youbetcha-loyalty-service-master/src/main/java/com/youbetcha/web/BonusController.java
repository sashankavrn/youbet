package com.youbetcha.web;

import com.youbetcha.model.ClaimBonusDto;
import com.youbetcha.service.BonusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bonus")
@Api(tags = "Bonus")
public class BonusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BonusController.class);

    @Autowired
    BonusService service;

    @PostMapping("/claim")
    @ApiOperation(value = "Award Bonus to user")
    public ResponseEntity<Object> awardBonus(@RequestBody ClaimBonusDto claimBonusDto) {
        return new ResponseEntity<>(service.claimBonus(claimBonusDto), HttpStatus.OK);
    }
}
