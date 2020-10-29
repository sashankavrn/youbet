package com.youbetcha.service;

import com.youbetcha.client.BonusClient;
import com.youbetcha.model.ClaimBonusDto;
import com.youbetcha.model.AwardBonusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BonusService {

    @Autowired
    BonusClient bonusClient;

    public AwardBonusResponse claimBonus(ClaimBonusDto claimBonusDto) {
        return bonusClient.claimBonus(claimBonusDto, "somesessionid")
                .orElseThrow(() -> new RuntimeException("Award Bonus error"));
    }
}
