package com.youbetcha.model.payments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusCheckDto {
	@JsonProperty("Action")
    private String action;
	@JsonProperty("MerchantReference")
    private String merchantReference;
	@JsonProperty("Signature")
    private String signature;
}
