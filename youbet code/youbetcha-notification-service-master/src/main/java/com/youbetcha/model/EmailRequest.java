package com.youbetcha.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private Person sender;
    @NonNull
    private List<Person> to;
    private List<Person> bcc;
    private List<Person> cc;
    private String htmlContent;
    private String textContent;
    private String subject;
    private Person replyTo;
    private Map<String, String> params;
    private long templateId;
}
