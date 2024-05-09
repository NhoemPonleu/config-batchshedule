package com.example.batchconfig.loan;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class LoanRequestDTO {
    private Integer loanTerm;
    private Double interestRate;
    private BigDecimal loanAmount;
    private String brandId;    private String creditOfficerName;
    private String laonType;
    private Long customerId;

}
