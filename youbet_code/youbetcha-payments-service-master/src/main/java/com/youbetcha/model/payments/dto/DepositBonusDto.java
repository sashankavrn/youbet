package com.youbetcha.model.payments.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositBonusDto {
    private String code;
    private Date activationDate;
    private Date expirationDate;
    private String titleKeyword;
    private String descriptionKeyword;
    private String termsKeyword;
    private String currencyCode;
    private String bonusAmount;
    private String minBonus;
    private String maxBonus;
    private String promoImage;
}
