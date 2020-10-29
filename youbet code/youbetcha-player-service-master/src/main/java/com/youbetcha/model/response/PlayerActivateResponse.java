package com.youbetcha.model.response;

import com.youbetcha.model.ErrorData;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerActivateResponse {
    private ErrorData errorData;
    private String sessionId;
    private Long userId;
    private Short success;
    private String timestamp;
    private String version;

}
