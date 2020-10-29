package com.youbetcha.model.games;

import java.util.Set;

import com.youbetcha.model.entity.Tag;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateGameRequest {
	private String gameName;
    @ApiModelProperty(name = "The category the game should be in", example = "LIVECASINO", required = false)
    private String category;
    @ApiModelProperty(name = "The game should be available to play enabled or disabled by BO", example = "true/false", required = false)
    private Boolean enabled;
    @ApiModelProperty(name = "The new game flag for BO", example = "true/false", required = false)
    private Boolean isNew;
    @ApiModelProperty(name = "The isJackPot the game should be in", example = "true/false", required = false)
    private Boolean isJackPot;
    @ApiModelProperty(name = "The hot game for BO", example = "true/false", required = false)
    private Boolean isHot;
    @ApiModelProperty(name = "The exclusive game for BO", example = "true/false", required = false)
    private Boolean isExclusive;
    @ApiModelProperty(name = "Tags to be associated with this game")
    private Set<Tag> tags; // = new HashSet<Tag>();

}
