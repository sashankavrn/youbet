package com.youbetcha.model.mapping;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "player_mapping")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PlayerMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String keyValue;
    private String payload;
}
