package com.youbetcha.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepositDto {
    private String version;
    private String partnerID;
    private String partnerKey;
    private String sessionId;
    private String userId;
    private BigDecimal requestAmount;
    private String requestCurrency;
    private String secretKey;
    private String transactionReference;
    private String note;
    private String userIp;
    private int isMobile;
    private String iovationBlackBox;
    private String paymentMethod;
    private long debitPayCardId;
    private int debitPayCardVendorId;
    private long creditAccountId;
    private String postBackURL;
    private String cancelURL;
    private String returnURL;
    private int applyBonusVendorID;
    private String applyBonusCode;
    private int depositSource;
}
