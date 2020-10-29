package com.youbetcha.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Entity
@Table(name = "deposit_bonus")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class DepositBonus extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private Date activationDate;
    private Date expirationDate;
    @Transient
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = BonusCountry.class)
    private Set<BonusCountry> activeCountries;
    @Transient
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = BonusUser.class)
    private Set<BonusUser> activeUsers;
    private Boolean enabled;
    private String internalDescription;
    private String titleKeyword;
    private String descriptionKeyword;
    private String termsKeyword;
    private String bonusAmount; // Can either be a number (eg. 100) or a percentage (eg. 200%)
    private Integer minBonus;
    private Integer maxBonus;
    private String promoImage;
    
}