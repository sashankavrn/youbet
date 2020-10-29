package com.youbetcha.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Entity
@Table(name = "register")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Player extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String email;
    @Transient
    private String password;
    @NotNull
    private String currency;
    @NotNull
    private String countryCode;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String mobilePrefix;
    @NotNull
    private String mobile;
    @NotNull
    private String birthDate;
    @NotNull
    private String city;
    @NotNull
    private String address1;
    private String address2;
    private String postalCode;
    @NotNull
    private String signupIp;
    private String currentStatus;
    private Boolean allowSmsNewsAndOffers;
    private Boolean allowNewsAndOffers;
    private String language;
    @NotNull
    private Boolean acceptTermsAndConditions;
    private Boolean bonusOptIn;
    private Long everymatrixUserId;
    @NotNull
    private Boolean enabled;
    private Integer depositCount;
    private Integer withdrawCount;
    
}