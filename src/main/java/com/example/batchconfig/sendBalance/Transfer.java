package com.example.batchconfig.sendBalance;

import com.example.batchconfig.sendBalance.constand.FeeTypeCode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String senderPhoneNumber;
    private String receiverPhoneNumber;
    private BigDecimal transferAmount;
    private BigDecimal afterTransferAmount;
    @Enumerated(EnumType.STRING)
    private FeeTypeCode feeTypeCode;
    private BigDecimal feeAmount;
    private LocalDate transferDate;
    private LocalTime transferTime;
    private String transferStatus;
    private String withdrawalYN;
    private UUID uuidPassword;
    private Integer userRegister;
    private Integer password;
    private String toAccountNumber;
    private String toAccountName;
}
