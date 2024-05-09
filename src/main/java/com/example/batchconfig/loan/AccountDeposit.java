package com.example.batchconfig.loan;

import com.example.batchconfig.account.Account;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "account_deposit")
@Getter
@Setter
public class AccountDeposit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deposit_amount")
    private BigDecimal depositAmount;

    @Column(name = "deposit_date")
    private LocalDate depositDate;

    // Define ManyToOne relationship with Loan
    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;
    @ManyToOne
    @JoinColumn(name = "account_number")
    private Account account;

    // Constructors, getters, and setters

    public AccountDeposit() {
    }

    public AccountDeposit(BigDecimal depositAmount, LocalDate depositDate, Loan loan) {
        this.depositAmount = depositAmount;
        this.depositDate = depositDate;
        this.loan = loan;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public LocalDate getDepositDate() {
        return depositDate;
    }

    public void setDepositDate(LocalDate depositDate) {
        this.depositDate = depositDate;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }
}
