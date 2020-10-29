package com.youbetcha.model.payments.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusChangeResponse {
    private String action;
    private int responseStatus;
    private String responseMessage;
    private String signature;
}
