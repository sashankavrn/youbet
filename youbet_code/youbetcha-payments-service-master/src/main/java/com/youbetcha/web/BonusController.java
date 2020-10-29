package com.youbetcha.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.youbetcha.model.dto.BonusValidateDto;
import com.youbetcha.model.payments.dto.DepositBonusDto;
import com.youbetcha.service.BonusService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(tags = "Deposit Bonuses")
public class BonusController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BonusController.class);

    @Autowired
    BonusService bonusService;
    
    @ApiOperation(value = "Get available bonuses for the given user")
    @GetMapping("/api/v1/bonus/{email}")
    public ResponseEntity<List<DepositBonusDto>> getDepositBonuses(@PathVariable String email) {   //@CookieValue(value = "youbetcha") String jwtToken) {
    	LOGGER.info("Attempting to fetch user details with email: {}", email);

    	List<DepositBonusDto> depositBonuses = bonusService.getAllDepositBonusesByUser(email);
    	
    	
//    	JwtTokenUtil tokenUtil = new JwtTokenUtil();
//    	String username = tokenUtil.getUsernameFromToken(jwtToken);
//    	LOGGER.info("D> Retrieve username {} from token.", username);
    	
    	// Get player based on session
//    	bonusService.getAllDepositBonusesByUser(sessionId);
    	// Query our list of available bonuses against this player
    	
    	// Return list of bonuses to FE for display
    	return new ResponseEntity<List<DepositBonusDto>>(depositBonuses, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Validate if the provided bonus code is ok to use")
    @PostMapping("/api/v1/bonus/validate")
    public ResponseEntity<Boolean> validateBonusCode(@RequestBody BonusValidateDto dto) {
    	LOGGER.info("Validating bonus code {} for user {}", dto.getCode(), dto.getEmail());
    	Boolean isValid = bonusService.validateBonusCode(dto.getCode(), dto.getEmail());
    	return new ResponseEntity<Boolean>(isValid, HttpStatus.OK);
    }
    

}
