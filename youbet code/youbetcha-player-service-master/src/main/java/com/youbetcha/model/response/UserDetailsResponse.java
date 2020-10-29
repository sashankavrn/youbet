package com.youbetcha.model.response;

import com.youbetcha.model.ErrorData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsResponse {
    private ErrorData errorData;
    private Short success;
    private String timestamp;
    private String personalId;
    private String version;
    private String activeStatus;
    private String email;
    private String userName;
    private String password;
    private String currency;
    private String countryCode;
    private String firstName;
    private String lastName;
    private String language;
    private String mobilePrefix;
    private String mobile;
    private String title;
    private String birthDate;
    private String city;
    private String address1;
    private String postalCode;
    private String signupIp;
}
