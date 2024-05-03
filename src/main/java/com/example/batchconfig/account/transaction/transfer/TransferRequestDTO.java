package com.example.batchconfig.account.transaction.transfer;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDTO {
    private String senderAccountId;
    private String receiverAccountId;
    private BigDecimal amount;
}