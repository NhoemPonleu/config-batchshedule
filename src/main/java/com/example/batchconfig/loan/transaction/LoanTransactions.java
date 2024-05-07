package com.example.batchconfig.loan.transaction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loan_transactions")
@Getter
@Setter
public class LoanTransactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loan_account_number")
    private String loanAccountNumber;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "payment")
    private BigDecimal payment;

    @Column(name = "interest")
    private BigDecimal interest;

    @Column(name = "principal")
    private BigDecimal principal;
    private String paymentType;
    // Constructors, getters, and setters
}
