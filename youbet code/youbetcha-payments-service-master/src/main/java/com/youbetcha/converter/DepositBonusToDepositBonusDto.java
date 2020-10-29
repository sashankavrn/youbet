package com.youbetcha.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.youbetcha.model.DepositBonus;
import com.youbetcha.model.payments.dto.DepositBonusDto;

@Component
public class DepositBonusToDepositBonusDto implements Converter<DepositBonus, DepositBonusDto> {

    public DepositBonusToDepositBonusDto() {
    }

    @Override
    public DepositBonusDto convert(DepositBonus source) {
    	DepositBonusDto bonus = new DepositBonusDto();
    	bonus.setActivationDate(source.getActivationDate());
    	bonus.setExpirationDate(source.getExpirationDate());
    	bonus.setCode(source.getCode());
    	bonus.setPromoImage(source.getPromoImage());
    	bonus.setTitleKeyword(source.getTitleKeyword());
    	bonus.setDescriptionKeyword(source.getDescriptionKeyword());
    	bonus.setTermsKeyword(source.getTermsKeyword());
    	bonus.setBonusAmount(source.getBonusAmount());
    	bonus.setMinBonus(source.getMinBonus().toString());
    	bonus.setMaxBonus(source.getMaxBonus().toString());
    	
    	return bonus;
    }

}
