package com.youbetcha.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Locale;

@Builder
@Data
public class NotificationDto {
    private String appUrl;
    private Locale locale;
    private String email;
    private String token;
}
