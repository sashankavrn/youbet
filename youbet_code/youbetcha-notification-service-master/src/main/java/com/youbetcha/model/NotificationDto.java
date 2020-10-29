package com.youbetcha.model;

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
