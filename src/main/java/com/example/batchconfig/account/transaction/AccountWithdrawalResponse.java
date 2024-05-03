package com.example.batchconfig.account.transaction;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AccountWithdrawalResponse {
    private String accountId;
    private BigDecimal transactionAmount;
    private String accountName;
    private LocalTime transactionTime;
    private LocalDate transactionDate;
}
