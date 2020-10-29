package com.youbetcha.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.youbetcha.model.DepositBonus;
import com.youbetcha.model.DepositBonusRequest;
import com.youbetcha.model.UpdateDepositBonusRequest;
import com.youbetcha.service.BonusService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/admin/bonus")
@Api(tags = "Deposit Bonus Admin functions")
public class BonusAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BonusAdminController.class);

    @Autowired
    BonusService bonusService;

    @GetMapping()
    @ApiOperation(value = "Get available bonuses")
    public List<DepositBonus> getAllBonuses() {
    	List<DepositBonus> bonuses = bonusService.getAllDepositBonuses();
    	LOGGER.debug("D> Returning {} bonuses in all", bonuses.size());
    	return bonuses;
    }

    @GetMapping("/id/{id}")
    @ApiOperation(value = "Get available bonuses for the given user's ID")
    public List<DepositBonus> getAllBonusesForUser(@PathVariable(name = "id") Long userId) {
    	List<DepositBonus> bonuses = bonusService.getAllDepositBonusesByUser(userId);
    	
    	return bonuses;
    }
    
    @PostMapping()
    @ApiOperation(value = "Create a new bonus entry")
    public DepositBonus createBonus(@RequestBody DepositBonusRequest depositBonusRequest) {
    	return bonusService.createDepositBonus(depositBonusRequest);
    }
    
    @PutMapping("/id/{id}")
    @ApiOperation(value = "Update an existing bonus")
    public DepositBonus updateBonus(@PathVariable(name = "id") Long bonusId, @RequestBody UpdateDepositBonusRequest updateDepositBonusRequest) {
    	return bonusService.updateDepositBonus(bonusId, updateDepositBonusRequest);
    }
    
    @DeleteMapping("/id/{id}")
    @ApiOperation(value = "Delete an existing bonus")
    public String deleteBonus(@PathVariable(name = "id") Long bonusId) {
    	bonusService.deleteDepositBonus(bonusId);
    	return null;
    }
    
    
}
