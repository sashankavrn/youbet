package com.youbetcha.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "tag")
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    private String keyWord;
    private Integer orderNumber;
    private String countryCode;
    private Boolean enabled;
//    @Builder.Default
//    @ManyToMany(mappedBy = "tags")
//    private Set<Game> games = new HashSet<>();
}
