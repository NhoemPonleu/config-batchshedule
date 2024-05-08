package com.example.batchconfig.account.transaction.transfer;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class TransactionResposnDTO {
    private String recieveFromAccount;
    private BigDecimal recieveAmount;
    private String transaction;
    private BigDecimal transactionAmount;
  //  private LocalDate transactionDate;
  //  private LocalTime transactionTime;
}
