package com.youbetcha.model.games.mix;


import lombok.Builder;

@Builder
@lombok.Data
public class Creation {
    private String time;
    private String newGameExpiryTime;
}
