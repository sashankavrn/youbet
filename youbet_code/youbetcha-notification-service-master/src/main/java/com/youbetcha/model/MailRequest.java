package com.youbetcha.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@Builder
public class MailRequest {
    private String firstName;
    private String lastName;
    private String emailAddress;
}
