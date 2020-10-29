package com.youbetcha.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcsAuthenticateResponse {
    private String sessionID;
    private String domainID;
}
