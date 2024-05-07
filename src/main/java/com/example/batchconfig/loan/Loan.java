package com.example.batchconfig.loan;

import com.example.batchconfig.brand.Brand;
import com.example.batchconfig.customer.Customer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@NoArgsConstructor
@Entity
@Getter
public class Loan {

    @Id
    @Column(name = "loan_account_number")
    private String loanAccountNumber;

    private Integer loanTerm;
    private Double interestRate;
    private BigDecimal loanAmount;
    private Double loanPercentage;
    private String feeRate;
    private LocalDate loanDate;
    private BigDecimal accruedInterest;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
}
