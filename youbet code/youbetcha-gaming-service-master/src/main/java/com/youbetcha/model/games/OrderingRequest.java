package com.youbetcha.model.games;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderingRequest {

    private Integer orderNumber;
    private Long gameId;
    private Long tagId;
    private String countryCode;

}
