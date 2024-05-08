package com.example.batchconfig.loan.transaction;

import com.example.batchconfig.loan.LoanScheduleItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class GenerateScheduleDTO {
    private String borrowerName;
    private String borrowerNumber;
    private BigDecimal laonAmount;
    private Double interestRate;
    private LocalDate borrowStrartDate;
    private LocalDate borrowEndDate;
    private Integer period;
    private String paymentMethod;
    private String creditOfficeName;
    private BigDecimal fee;
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
    public GenerateScheduleDTO(String borrowerName,BigDecimal laonAmount, String borrowerNumber, List<LoanScheduleItem> loanScheduleItems, BigDecimal totalInterest,
                               BigDecimal totalAmount,LocalDate borrowStrartDate,LocalDate borrowEndDate,Integer period,
                               String creditOfficeName,Double interestRate,String paymentMethod,BigDecimal fee) {
        this.borrowerName = borrowerName;
        this.laonAmount=laonAmount;
        this.borrowerNumber = borrowerNumber;
        this.loanScheduleItems = loanScheduleItems;
        this.totalInterest = totalInterest;
        this.totalAmount = totalAmount;
        this.borrowStrartDate = borrowStrartDate;
        this.borrowEndDate = borrowEndDate;
        this.period = period;
        this.creditOfficeName=creditOfficeName;
        this.interestRate=interestRate;
        this.paymentMethod=paymentMethod;
        this.fee=fee;
    }

//    public GenerateScheduleDTO(String firstName, String loanAccountNumber, List<LoanScheduleItem> loanScheduleItems, BigDecimal totalInterest, LocalDate loanDate, Integer loanTerm, LocalDate endate, BigDecimal totalAmount) {
//        this.borrowerName = borrowerName;
//        this.borrowerNumber = borrowerNumber;
//        this.loanScheduleItems = loanScheduleItems;
//        this.totalInterest = totalInterest;
//        this.totalAmount = totalAmount;
//        this.borrowStrartDate = borrowStrartDate;
//        this.borrowEndDate = borrowEndDate;
//        this.period = period;
//    }

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
