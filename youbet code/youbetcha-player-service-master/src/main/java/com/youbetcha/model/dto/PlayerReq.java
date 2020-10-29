package com.youbetcha.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerReq {
    private String email;
    private String userName;
    private String password;
    private String currency;
    private String countryCode;
    private String firstName;
    private String lastName;
    private String mobilePrefix;
    private String mobile;
    private String title;
    private String birthDate;
    private String city;
    private String address1;
    private String address2;
    private String postalCode;
    private String signupIp;
    private String allowSmsNewsAndOffers;
    private String allowNewsAndOffers;
    private String language;

    @Override
    public String toString() {
        return "PlayerDto{" +
                "email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", currency='" + currency + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mobilePrefix='" + mobilePrefix + '\'' +
                ", mobile='" + mobile + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", city='" + city + '\'' +
                ", address1='" + address1 + '\'' +
                ", address2='" + address2 + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", signupIp='" + signupIp + '\'' +
                ", allowSmsNewsAndOffers='" + allowSmsNewsAndOffers + '\'' +
                ", allowNewsAndOffers='" + allowNewsAndOffers + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}