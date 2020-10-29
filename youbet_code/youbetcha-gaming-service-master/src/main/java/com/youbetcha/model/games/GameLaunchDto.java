package com.youbetcha.model.games;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameLaunchDto {
    @ApiModelProperty(name = "Slug indicates an alphanumeric string that uniquely identifies a game", required = true, example = "gonzos-quest-html5")
    private String slug;
    @Nullable
    @ApiModelProperty(name = "The language code in which the casino game will be displayed", example = "es")
    private String language;
    @ApiModelProperty(name = "Game is in fun mode", required = true, example = "true")
    private boolean funmode;
    @Nullable
    @ApiModelProperty(name = "Table id (for casino games)", required = false, example = "7934")
    private String tableId;
    @Nullable
    @ApiModelProperty(name = "Return URL when the player clicks on home", required = false, example = "http://beta.youbetcha.com/games")
    private String lobbyURL;
}
