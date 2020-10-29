package com.youbetcha.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class GameProperties {
    private String funGameURL;
    private String backgroundImage;
    private String logo;
    private String thumbnail;
}
