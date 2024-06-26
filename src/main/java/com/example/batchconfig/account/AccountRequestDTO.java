package com.example.batchconfig.account;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class AccountRequestDTO {
    private String accountName;
    private String brandCode;
    private BigDecimal balance;
    private String accountType;
    private Integer identityCardNo;
}
