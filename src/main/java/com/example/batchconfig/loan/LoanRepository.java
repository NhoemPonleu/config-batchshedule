package com.example.batchconfig.loan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    boolean existsByLoanAccountNumber(String accountNumber);
    Optional<Loan> findByLoanAccountNumber(String loanAccountNumber);
}
