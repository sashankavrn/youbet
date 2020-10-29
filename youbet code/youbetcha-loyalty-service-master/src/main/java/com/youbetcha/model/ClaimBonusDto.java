package com.youbetcha.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClaimBonusDto {
    private Long userID;
    private String currency;
    private String bonusCode;
    private String bonusWalletId;
}
