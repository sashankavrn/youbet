package com.youbetcha.model.games.tags;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagRequest {
    private String name;
    private String keyWord;
    private String countryCode;
    private Integer orderNumber;
    private Boolean enabled;
}
