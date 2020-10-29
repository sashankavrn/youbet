package com.youbetcha.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "message")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Message extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private boolean seen;
    private String content;
}
