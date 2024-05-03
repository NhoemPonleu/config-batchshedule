package com.example.batchconfig.account.transaction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class AccountTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountId;
    private BigDecimal acruedInterest;
    private BigDecimal acruedAmount;
    private BigDecimal firstAmount;
    private BigDecimal transactionAmount;
    private BigDecimal transactionTotalAmount;
    private BigDecimal feeAmount;
    private String fromAccountId;
    private String toAccountId;
    private LocalDate transactionDate;
    private String transactionType;
    private LocalDateTime transactionTime;
    private LocalTime registerTime;
    private Long transactionSeqNo;

}
