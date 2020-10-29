package com.youbetcha.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

@Data
@Builder
@Entity
@javax.persistence.Table(name = "em_game")
@AllArgsConstructor
@NoArgsConstructor
public class EMGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long dataId;
    @NotNull
    private String slug;
    @NotNull
    private String gameName;
    @NotNull
    private String gameId;
    private boolean enabled;
    private Float popularity;
    private String categories;
    private String tags;
    private String restrictedTerritories;
    private String gameProperties;
    private String terminal;
    private Timestamp updatedTs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EMGame emGame = (EMGame) o;
        return Objects.equals(dataId, emGame.dataId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId);
    }
}
