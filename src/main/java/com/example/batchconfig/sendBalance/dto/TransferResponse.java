package com.example.batchconfig.sendBalance.dto;

import com.example.batchconfig.security.user.UserRequestDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class TransferResponse {
    private String senderPhoneNumber;
    private String receiverPhoneNumber;
    private BigDecimal transferAmount;
    private BigDecimal feeAmount;
    private LocalDate transferDate;
    private LocalTime transferTime;
    private UserRequestDTO userRequestDTO;
}
