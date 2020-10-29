package com.youbetcha.model.account;

import com.youbetcha.model.ErrorData;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountUpdateResponse {
    private ErrorData errorData;
    private Short success;
    private String timestamp;

    private Boolean notificationsEnabled;
    private String address1;
    private String address2;
    private String city;
    private String country;
    private String postalCode;
    private String mobilePrefix;
    private String mobile;
    
}