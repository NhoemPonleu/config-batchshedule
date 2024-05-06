package com.example.batchconfig.loan;

import com.example.batchconfig.brand.Brand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "loan_account_number")
    private String loanAccountNumber;
    private Integer loanTerm;
    private Double interestRate;
    private BigDecimal loanAmount;
    private String feeRate;
    @ManyToOne
    private Brand brand;
}
