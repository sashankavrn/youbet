package com.youbetcha.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ErrorData {
    private List<String> errorDetails;
    private Short errorCode;
    private String errorMessage;
    private Integer logId;
}
