package com.example.batchconfig.loan;

import com.example.batchconfig.account.Account;
import com.example.batchconfig.brand.Brand;
import com.example.batchconfig.customer.Customer;
import com.example.batchconfig.loan.transaction.LoanSheduleTypeCodeq;
import com.example.batchconfig.security.user.UserRequestDTO;
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
    private BigDecimal feeRate;
    private LocalDate loanDate;
    private BigDecimal accruedInterest;
    private String creditOfficerName;
    @Enumerated(EnumType.STRING)
    private LoanSheduleTypeCodeq loanScheduleType;
    private Integer registerTellerId;
    private String registerTellerName;
    private Integer identityNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_account_number", referencedColumnName = "accountNumber")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
}
