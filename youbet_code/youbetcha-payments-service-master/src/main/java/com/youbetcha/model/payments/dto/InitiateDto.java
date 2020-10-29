package com.youbetcha.model.payments.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.youbetcha.model.payments.InitiateType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitiateDto {
    private BigDecimal amount;
    @ApiModelProperty(required = true)
    private InitiateType initiateType;
    @JsonIgnore
    private String ipAddress;
    @ApiModelProperty(required = false)
    private String bonusCode;
}
