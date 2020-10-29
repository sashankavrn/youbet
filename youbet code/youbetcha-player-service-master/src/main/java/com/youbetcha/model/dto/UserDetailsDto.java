package com.youbetcha.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {
    private String personalId;
    private String email;
    private String countryCode;
    private String firstName;
    private String lastName;
    private String mobilePrefix;
    private String mobile;
    private String city;
    private String address1;
    private String postalCode;
    private String birthDate;
}
