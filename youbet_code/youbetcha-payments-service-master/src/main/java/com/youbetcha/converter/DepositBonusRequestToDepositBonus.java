package com.youbetcha.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.youbetcha.model.DepositBonus;
import com.youbetcha.model.DepositBonusRequest;

@Component
public class DepositBonusRequestToDepositBonus implements Converter<DepositBonusRequest, DepositBonus> {

    public DepositBonusRequestToDepositBonus() {
    }

    @Override
    public DepositBonus convert(DepositBonusRequest source) {
    	DepositBonus bonus = new DepositBonus();
    	bonus.setActivationDate(source.getActivationDate());
    	bonus.setExpirationDate(source.getExpirationDate());
    	bonus.setCode(source.getCode().toUpperCase());
    	bonus.setEnabled(source.getEnabled());
    	bonus.setInternalDescription(source.getInternalDescription());
    	bonus.setPromoImage(source.getPromoImage());
    	bonus.setTitleKeyword(source.getTitleKeyword());
    	bonus.setDescriptionKeyword(source.getDescriptionKeyword());
    	bonus.setTermsKeyword(source.getTermsKeyword());
    	bonus.setBonusAmount(source.getBonusAmount());
    	bonus.setMinBonus(source.getMinBonus());
    	bonus.setMaxBonus(source.getMaxBonus());

    	return bonus;
    }

}
