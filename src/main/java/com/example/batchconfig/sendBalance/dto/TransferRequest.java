package com.example.batchconfig.sendBalance.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class TransferRequest {
    private String senderPhoneNumber;
    private String receiverPhoneNumber;
    private BigDecimal transferAmount;
    private BigDecimal feeAmount;
    private String feeTypeCode;
}
