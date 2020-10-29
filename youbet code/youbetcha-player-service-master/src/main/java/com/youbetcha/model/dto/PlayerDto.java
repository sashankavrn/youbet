package com.youbetcha.model.dto;

import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerDto {
    @JsonIgnore
    private Long id;
    @Email(message = "Email should be valid")
    @ApiModelProperty(required = true, example = "testingacc@sharklasers.com")
    private String email;
    @JsonIgnore
    private String userName;
    @ApiModelProperty(required = true, example = "Password123")
    private String password;
    @ApiModelProperty(required = true, example = "EUR")
    private String currency;
    @ApiModelProperty(required = true, example = "IT")
    private String countryCode;
    @ApiModelProperty(required = true, example = "John")
    private String firstName;
    @ApiModelProperty(required = true, example = "Doyle")
    private String lastName;
    @ApiModelProperty(required = true, example = "+39")
    private String mobilePrefix;
    @ApiModelProperty(required = true, example = "764696969")
    private String mobile;
    @JsonIgnore
    private String title;
    @ApiModelProperty(required = true, example = "1989-06-06")
    private String birthDate;
    @ApiModelProperty(required = true, example = "Rome")
    private String city;
    @ApiModelProperty(required = true, example = "test1")
    private String address1;
    @ApiModelProperty(required = true, example = "test2")
    private String address2;
    @ApiModelProperty(required = true, example = "969696")
    private String postalCode;
    @ApiModelProperty(required = true, example = "85.9.7.222", hidden = true)
    private String signupIp;
    @ApiModelProperty(example = "true", dataType = "boolean")
    private Boolean allowSmsNewsAndOffers;
    @ApiModelProperty(example = "true", dataType = "boolean")
    private Boolean allowNewsAndOffers;
    @ApiModelProperty(example = "en")
    private String language;
    private Boolean acceptTCs;
    private Boolean bonusOptIn;

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
                ", bonusOptIn='" + bonusOptIn + '\'' +
                ", acceptTCs='" + acceptTCs + '\'' +
                ", allowSmsNewsAndOffers='" + allowSmsNewsAndOffers + '\'' +
                ", allowNewsAndOffers='" + allowNewsAndOffers + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}