package com.example.batchconfig.sendBalance.dto;

import com.example.batchconfig.security.user.UserRequestDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class WithdrawalResponse {
    private String reciverPhoneNumber;
    private BigDecimal withdrawalAmount;
    private LocalDate withdrawalDate;
    private LocalTime withdrawalTime;
    private String transactionTypeCode;
    UserRequestDTO userRequestDTO;
}
