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
public class LoginPlayerResponse {
    private ErrorData errorData;
    private String firstName;
    private String language;
    private String lastName;
    private String sessionId;
    private String registrationDate;
    private Short success;
    private String timestamp;
    private Long userId;
    private String version;
}
