package com.youbetcha.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorData {
    private List<ErrorDetails> errorDetails;
    private Short errorCode;
    private String errorMessage;
    private Integer logId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ErrorDetails {
        private String errorDetail;
    }
}
