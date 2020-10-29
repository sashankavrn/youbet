package com.youbetcha.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {
    @NotNull
    private String email;
    @NotNull
    private Map<String, String> attributes;
    @NotNull
    private boolean emailBlacklisted;
    @NotNull
    private boolean smsBlacklisted;
    @NotNull
    private int[] listIds;
    @NotNull
    private boolean updateEnabled;
    @NotNull
    private String[] smtpBlacklistSender;
}
