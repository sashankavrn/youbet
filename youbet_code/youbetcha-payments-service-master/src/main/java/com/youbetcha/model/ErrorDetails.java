package com.youbetcha.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "error_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String errorDetail;
}
