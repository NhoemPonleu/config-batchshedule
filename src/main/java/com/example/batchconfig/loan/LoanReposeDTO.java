package com.example.batchconfig.loan;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class LoanReposeDTO {
    private String loanAccountNumber;
    private BigDecimal loanAmount;
    private Integer loanTerm;


}
