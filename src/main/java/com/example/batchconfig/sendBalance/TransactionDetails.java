package com.example.batchconfig.sendBalance;

import com.example.batchconfig.sendBalance.constand.WithdrawalTransferTypeCode;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class TransactionDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private WithdrawalTransferTypeCode transactionType;
    private String senderPhoneNumber;
    private String receiverPhoneNumber;
    private BigDecimal transactionAmount;
    private BigDecimal transactionTotalAmount;
    private BigDecimal feeAmount;
    private LocalDate transactionDate;
    private LocalTime transactionTime;
}
