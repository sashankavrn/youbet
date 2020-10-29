package com.youbetcha.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "country")
@AllArgsConstructor
@NoArgsConstructor
public class Country {

    @Id
    private Long id;
    @NotNull
    private String iso2;
    @NotNull
    private String iso3;
    @NotNull
    private String name;
    @NotNull
    private String phoneCode;
    @NotNull
    private Boolean active;
    private Integer orderNumber;

    public String getPhoneCodeWithPrefix() {
        return "+" + phoneCode;
    }
}