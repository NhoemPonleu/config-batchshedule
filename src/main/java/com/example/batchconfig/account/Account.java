package com.example.batchconfig.account;

import com.example.batchconfig.customer.Customer;
import com.example.batchconfig.loan.AccountDeposit;
import com.example.batchconfig.loan.Loan;
import com.example.batchconfig.security.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Account {
    @Id
    private String accountNumber; // Making accountNumber as primary key
    private String accountName;
    private String brandCode;
    private BigDecimal balance;
    private String accountType;
    private BigDecimal firstAmount;
    private BigDecimal lastAmount;
    private Integer userId;
    private String accountStatusYN;

    @OneToMany(mappedBy = "account")
    private Set<Loan> loans;

    @OneToMany(mappedBy = "account")
    private Set<AccountDeposit> accountDeposits; // Link with AccountDeposit

    @ManyToOne // Many accounts can belong to one customer
    @JoinColumn(name = "customer_id")
    private Customer customer; // Link with Customer

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModified;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private Integer createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private Integer lastModifiedBy;
}
