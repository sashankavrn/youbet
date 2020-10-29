package com.youbetcha.model.games.mix;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Property {
    List<String> terminal;
    List<String> jurisdictions;
    boolean launchGameInHtml5;
    private float width;
    private float height;
    private String license;
}
