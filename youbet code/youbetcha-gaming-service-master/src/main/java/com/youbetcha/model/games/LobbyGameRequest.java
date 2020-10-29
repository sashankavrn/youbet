package com.youbetcha.model.games;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LobbyGameRequest {
    @ApiModelProperty(name = "The Game id from the Game database table", example = "400")
    private Long gameId;
    @ApiModelProperty(name = "The country code for the lobby game (or ALL)", example = "GB")
    private String countryCode;
    @ApiModelProperty(name = "Lobby game is top game", example = "false")
    private boolean isTopGame;
}
