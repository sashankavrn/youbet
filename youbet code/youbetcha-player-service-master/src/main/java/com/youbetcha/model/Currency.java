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
@Table(name = "currency")
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    @Id
    private Long id;
    @NotNull
    private String code;
    @NotNull
    private String description;
    @NotNull
    private Boolean active;
}