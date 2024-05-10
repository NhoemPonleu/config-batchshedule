package com.example.batchconfig.sendBalance.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithDrawalRequest {
    private String reciverPhoneNumber;
    private Integer requestPassword;
    private BigDecimal withdrawalAmount;

}
