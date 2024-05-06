package com.example.batchconfig.loan.transaction;

import com.example.batchconfig.loan.Loan;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
@Entity
@Getter
@Setter
public class LoanTransactions {
    @Id
    private Long id;
    @JoinColumn(name = "loan_account_number", referencedColumnName = "loan_account_number")
    private String loan;
    private BigDecimal feeAmount;
    private BigDecimal FirstBalance;
    private BigDecimal afterBalance;
    private BigDecimal totalPayment;
    private LocalDate transactionDate;
    private LocalTime transactionTime;
}
