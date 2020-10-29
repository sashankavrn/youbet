package com.youbetcha.model.payments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitiateRequest {
    private long merchantId;
    private String merchantReference;
    private String customerId;
    private String firstName;
    private String lastName;
    private String userName;
    private String personalNumber;
    private String emailAddress;
    private String birthDate;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String countryCode;
    private String customerGroups;
    private String channel;
    private String ipAddress;
    private String registrationIpAddress;
    private String registrationDate;
    private String paymentMethod;
    @Builder.Default
    private boolean allowPaySolChange = true;
    private BigDecimal amount;
    private String currency;
    private String cardToken;
    private String cardholderName;
    private String expirationMonth;
    private String expirationYear;
    private String successUrl;
    private String failUrl;
    private String callbackUrl;
    private String cancelUrl;
    private String checkStatusUrl;
    private String signature;
    private String language;
    private ArrayList<MerchantInputFields> merchantInputFields;
}
