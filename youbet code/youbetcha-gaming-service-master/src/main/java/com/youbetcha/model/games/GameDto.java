package com.youbetcha.model.games;

import com.youbetcha.model.entity.GameProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {
	private Long id;
    private String slug;
    private Long tableId;
    private Integer orderNumber;
    private GameProperties properties;
}
