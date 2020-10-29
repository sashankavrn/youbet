package com.youbetcha.model.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailsDto {
    private String sessionId;
    private String address1;
    private String address2;
    private String city;
    private String country;
    private String postalCode;
    private String mobilePrefix;
    private String mobile;
}
