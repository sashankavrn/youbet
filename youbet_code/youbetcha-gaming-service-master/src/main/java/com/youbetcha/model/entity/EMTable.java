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
@javax.persistence.Table(name = "em_table")
@AllArgsConstructor
@NoArgsConstructor
public class EMTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private Long tableId;
    @NotNull
    private String slug;
    private String tableName;
    @NotNull
    private String gameId;
    private boolean isOpen;
    private String category;
    private boolean enabled;
    private String categories;
    private String restrictedTerritories;
    private String tableProperties;
    private String terminal;
    private Timestamp updatedTs;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EMTable emTable = (EMTable) o;
        return Objects.equals(tableId, emTable.tableId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableId);
    }
}
