package com.youbetcha.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Player extends PlayerAuditable<String> {

    private Long id;
    private String email;
    private String password;
    private String currency;
    private String countryCode;
    private String firstName;
    private String lastName;
    private String mobilePrefix;
    private String mobile;
    private String birthDate;
    private String city;
    private String address1;
    private String address2;
    private String postalCode;
    private String signupIp;
    private String currentStatus;
    private boolean allowSmsNewsAndOffers;
    private boolean allowNewsAndOffers;
    private String language;
    private boolean acceptTermsAndConditions;
    private boolean bonusOptIn;
    private Long everymatrixUserId;
    private boolean enabled;
    private int depositCount;
    private int withdrawCount;
}