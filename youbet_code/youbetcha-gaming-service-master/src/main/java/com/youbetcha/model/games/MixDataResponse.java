package com.youbetcha.model.games;

import com.youbetcha.model.games.mix.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MixDataResponse {
    private int domainID;
    private String action;
    private String type;
    private long id;
    private int seq;
    private Data data;
    //private Report report;
    private String betLimits;
    //private Presentation presentation;

}

