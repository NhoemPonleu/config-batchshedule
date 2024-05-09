package com.example.batchconfig.account.transaction.transfer;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class DepositRequestDTO {
  //  private String accountNo;
    private BigDecimal depositAmount;
}
