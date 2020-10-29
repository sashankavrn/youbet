package com.youbetcha.model.event;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PushNotification {
    private String emailAddress;
    private boolean email;
    private boolean sms;
    private boolean inApp;
    private String content;
    private boolean seen;
}
