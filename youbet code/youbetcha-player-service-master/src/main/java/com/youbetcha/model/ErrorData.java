package com.youbetcha.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorData {
    private List<ErrorDetails> errorDetails;
    private Short errorCode;
    private String errorMessage;
    private Integer logId;
}
