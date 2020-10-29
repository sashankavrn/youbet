package com.youbetcha.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "error_data")
@Entity
public class ErrorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ErrorDetails> errorDetails;
    private Short errorCode;
    private String errorMessage;
    private Integer logId;
    
	@Override
	public String toString() {
		return "ErrorData [id=" + id + ", errorDetails=" + errorDetails + ", errorCode=" + errorCode + ", errorMessage="
				+ errorMessage + ", logId=" + logId + "]";
	}
    
}
