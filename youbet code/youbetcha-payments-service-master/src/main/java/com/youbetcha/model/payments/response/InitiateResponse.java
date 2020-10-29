package com.youbetcha.model.payments.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InitiateResponse {
    @JsonProperty(value = "CashierUrl")
    private String CashierUrl;
    @JsonProperty(value = "ResponseDisplayText")
    private String ResponseDisplayText;
    @JsonProperty(value = "RequestId")
    private String RequestId;
    @JsonProperty(value = "ResponseCode")
    private Integer ResponseCode;
    @JsonProperty(value = "ResponseMessage")
    private String ResponseMessage;
    @JsonProperty(value = "Signature")
    private String Signature;
    @JsonProperty(value = "TransactionCode")
    private String TransactionCode;
}
