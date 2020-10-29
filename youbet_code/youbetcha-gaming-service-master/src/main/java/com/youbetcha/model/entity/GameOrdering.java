package com.youbetcha.model.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Builder
@Entity
@Table(name = "game_ordering")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GameOrdering {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer orderNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    private Game game;
    @ManyToOne(fetch = FetchType.EAGER)
    private Tag tag;
    private String countryCode;
    private Timestamp createdDate;

}
