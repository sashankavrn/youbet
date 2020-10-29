package com.youbetcha.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionIdValidationResponse {
    private ErrorData errorData;
    private Short success;
    private String timestamp;
    private String version;
    private String firstName;
    private Integer isActive;
    private String language;
    private String lastName;
    private Long userId;
    private String userName;
}
