package com.example.batchconfig.loan.transaction;

import com.example.batchconfig.loan.LoanScheduleItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GenerateScheduleDTO {
    private String borrowerName;
    private String borrowerNumber;
    private List<LoanScheduleItem> loanScheduleItems;
    private BigDecimal totalInterest;
    private BigDecimal totalAmount;
    public GenerateScheduleDTO() {
        // Default constructor
    }
    // Existing constructor
    public GenerateScheduleDTO(String borrowerName, String borrowerNumber, List<LoanScheduleItem> loanScheduleItems) {
        this.borrowerName = borrowerName;
        this.borrowerNumber = borrowerNumber;
        this.loanScheduleItems = loanScheduleItems;

        // Calculate total interest and total amount
        calculateTotals();
    }

    // Additional constructor
    public GenerateScheduleDTO(String borrowerName, String borrowerNumber, List<LoanScheduleItem> loanScheduleItems, BigDecimal totalInterest, BigDecimal totalAmount) {
        this.borrowerName = borrowerName;
        this.borrowerNumber = borrowerNumber;
        this.loanScheduleItems = loanScheduleItems;
        this.totalInterest = totalInterest;
        this.totalAmount = totalAmount;
    }

    private void calculateTotals() {
        BigDecimal totalInterest = BigDecimal.ZERO;
        BigDecimal totalPrincipal = BigDecimal.ZERO;

        for (LoanScheduleItem item : loanScheduleItems) {
            totalInterest = totalInterest.add(item.getInterest());
            totalPrincipal = totalPrincipal.add(item.getPrincipal());
        }

        this.totalInterest = totalInterest;
        this.totalAmount = totalInterest.add(totalPrincipal);
    }
}
