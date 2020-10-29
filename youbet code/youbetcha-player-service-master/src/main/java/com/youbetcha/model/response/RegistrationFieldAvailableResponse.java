package com.youbetcha.model.response;

import com.youbetcha.model.ErrorData;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationFieldAvailableResponse {
    private ErrorData errorData;
    private int isAvailable;
    private int success;
    private String timestamp;
    private String version;
}
