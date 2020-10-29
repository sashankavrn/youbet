package com.youbetcha.model.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusChangeDto {
    @JsonProperty("Action")
    private String action;
    @JsonProperty("MerchantReference")
    private String merchantReference;
    @JsonProperty("PaymentVendor")
    private String paymentVendor;
    @JsonProperty("RequestedAmount")
    private BigDecimal requestedAmount;
    @JsonProperty("ConfirmedAmount")
    private BigDecimal confirmedAmount;
    @JsonProperty("VendorRequestedAmount")
    private BigDecimal vendorRequestedAmount;
    @JsonProperty("VendorConfirmedAmount")
    private BigDecimal vendorConfirmedAmount;
    @JsonProperty("Amount")
    private BigDecimal amount;
    @JsonProperty("VendorCurrency")
    private String vendorCurrency;
    @JsonProperty("Currency")
    private String currency;
    @JsonProperty("TransactionCode")
    private String transactionCode;
    @JsonProperty("TransactionReference")
    private String transactionReference;
    @JsonProperty("VendorReference")
    private String vendorReference;
    @JsonProperty("Status")
    private int status;
    @JsonProperty("ResponseCode")
    private int responseCode;
    @JsonProperty("ResponseMessage")
    private String responseMessage;
    @JsonProperty("VendorCode")
    private String vendorCode;
    @JsonProperty("VendorMessage")
   private String vendorMessage;
    @JsonProperty("Token")
    private String token;
    @JsonProperty("AccountNumber")
    private String accountNumber;
    @JsonProperty("PaymentAccountFields")
    private Map<String, String> paymentAccountFields;
    @JsonProperty("Signature")
    private String signature;
}
