package com.youbetcha.model.response;

import com.youbetcha.model.ErrorData;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PlayerHashResponse {
    private String URL;
    private ErrorData errorData;
    private Short success;
    private String timestamp;
    private String version;
}
