package com.example.batchconfig.account.transaction.transfer;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
@Data
public class TransferResponse {
    private String senderAccountId;
    private String receiverAccountId;
    private String accountSenderName;
    private String accountReceiverName;
    private BigDecimal amount;
    private BigDecimal feeAmount;
    private BigDecimal transactionTotalAmount;
    private LocalTime transactionTime;
    private LocalDate transactionDate;
}
