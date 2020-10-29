package com.youbetcha.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "login")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String language;
    private String lastName;
    private String registrationDate;
    private Short success;
    private String timestamp;
    private Long userId;
}
