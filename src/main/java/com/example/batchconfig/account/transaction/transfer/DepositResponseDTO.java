package com.example.batchconfig.account.transaction.transfer;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class DepositResponseDTO {
    private String accountNumber;
    private BigDecimal depositAmount;
    private String transactionType;
    private LocalDate transactionDate;
    private LocalTime transactionTime;
}
