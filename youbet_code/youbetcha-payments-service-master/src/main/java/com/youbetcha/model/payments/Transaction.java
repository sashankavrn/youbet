package com.youbetcha.model.payments;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.youbetcha.util.TransactionStatus;
import com.youbetcha.util.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "transaction")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TransactionStage transactionStage;
    private String merchantReference;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private BigDecimal amount;
    private String signature;
    private String transactionCode;
    private String requestId;
    private Long playerId;
    private String ip;
    private String providerResponse;
    private LocalDateTime retryTime;
    private Integer retryCount;
    private String code;
    @Builder.Default private String createdBy = "System";
    @Builder.Default private LocalDateTime createdDate = LocalDateTime.now();
}
