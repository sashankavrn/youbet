package com.youbetcha.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {

    private String oldPlainTextPassword;
    private String plainTextPassword;
    private String sessionId;

    @Override
    public String toString() {
        return "ChangePasswordDto{" +
                "sessionId='" + sessionId + '\'' +
                '}';
    }
}
