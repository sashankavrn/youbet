package com.youbetcha.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AwardBonusResponse {
    @SerializedName("VendorBonusId")
    private String vendorBonusId;
    @SerializedName("Success")
    private boolean success;
    @SerializedName("Message")
    private String message;
    @SerializedName("VendorError")
    private String vendorError;
    @SerializedName("ErrorName")
    private String errorName;
    @SerializedName("ErrorCode")
    private Integer errorCode;
    @SerializedName("AdditionalParameters")
    private String additionalParameters;
}
