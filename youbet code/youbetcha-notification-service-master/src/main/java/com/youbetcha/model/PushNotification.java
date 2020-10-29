package com.youbetcha.model;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "push_notification")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class PushNotification extends Auditable<String>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emailAddress;
    private boolean email;
    private boolean sms;
    private boolean inApp;
    private String content;
    private boolean seen;
}
