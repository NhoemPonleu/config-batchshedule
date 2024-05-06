package com.example.batchconfig.loan;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public  class LoanScheduleItem {
    private int period; // Period number
    private LocalDate payDate;
    private BigDecimal payment; // Payment amount
    private BigDecimal interest; // Interest for the period
    private BigDecimal principal; // Principal for the period
    private BigDecimal remainingBalance; // Remaining balance after the period

    // Constructor
    public LoanScheduleItem(int period,LocalDate payDate, BigDecimal payment, BigDecimal interest, BigDecimal principal, BigDecimal remainingBalance) {
        this.period = period;
        this.payDate = payDate;
        this.payment = payment;
        this.interest = interest;
        this.principal = principal;
        this.remainingBalance = remainingBalance;
    }
}
