package com.youbetcha.model.games;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {
	private Long id;
    private Integer orderNumber;
    private String name;
    private String keyWord;
}
