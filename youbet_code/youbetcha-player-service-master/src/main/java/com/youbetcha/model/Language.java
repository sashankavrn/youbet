package com.youbetcha.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Entity
@Table(name = "language")
@AllArgsConstructor
@NoArgsConstructor
public class Language {

    @Id
    private Long id;
    @NotNull
    private String locale;
    @NotNull
    private String description;
    @NotNull
    private Boolean active;
}
