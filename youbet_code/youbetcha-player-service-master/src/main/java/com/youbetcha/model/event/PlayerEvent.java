package com.youbetcha.model.event;

import com.youbetcha.model.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PlayerEvent {
    private Player player;
    private String merchantReference;
}
