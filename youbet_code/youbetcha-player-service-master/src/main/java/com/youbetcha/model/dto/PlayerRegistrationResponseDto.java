package com.youbetcha.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRegistrationResponseDto {
    private ErrorData errorData;
    private Short success;
    private String timestamp;
    private Long userId;
    private String version;
    private List<PlayerErrorDto> playerErrorData;
}
