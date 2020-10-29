package com.youbetcha.model;

import java.util.Date;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseDepositBonusRequest {
    private Date activationDate;
    private Date expirationDate;
    private Set<String> activeCountries; // = new HashSet<String>();
    private Set<Long> activeUsers;
    private Boolean enabled;
    private String internalDescription;
    private String titleKeyword;
    private String descriptionKeyword;
    private String termsKeyword;
    private String currencyCode;
    private String bonusAmount;
    private Integer minBonus;
    private Integer maxBonus;
    private String promoImage;

}
