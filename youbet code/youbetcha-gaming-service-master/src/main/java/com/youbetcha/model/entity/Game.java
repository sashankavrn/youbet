package com.youbetcha.model.entity;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Entity
@Table(name = "game")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long dataId;
    @NotNull
    private String slug;
    @NotNull
    private String gameId;
    private String gameName;
    private String category;
    private String restrictedTerritories;
    private String gameProperties;
    private String terminal;
    @Transient
    private Integer orderNumber;
    private Boolean isOpen;
    private Boolean enabled;
    private Boolean isNew;
    private Boolean isJackpot;
    private Boolean isHot;
    private Boolean isExclusive;
    private Timestamp updatedTs;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "game_tag",
            joinColumns = { @JoinColumn(name = "game_id", referencedColumnName = "id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id", referencedColumnName = "id") }
    )
    private Set<Tag> tags; // = new HashSet<Tag>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game EMGame = (Game) o;
        return Objects.equals(dataId, EMGame.dataId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId);
    }
}
