package com.youbetcha.model.response;

import com.youbetcha.model.ErrorData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRegistrationResponse {
    private Long id;
    private ErrorData errorData;
    private Short success;
    private String timestamp;
    private Long userId;
    private String version;
}
