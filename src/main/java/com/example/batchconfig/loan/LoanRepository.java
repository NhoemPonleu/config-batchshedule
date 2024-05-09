package com.example.batchconfig.loan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    boolean existsByLoanAccountNumber(String accountNumber);
    Optional<Loan> findByLoanAccountNumber(String loanAccountNumber);
    List<Loan> findByIdentityNo(Integer identityNo);
    List<Loan> findByDepositAccountNumberIn(List<String> depositAccountNumbers);
    @Query("SELECT l FROM Loan l JOIN l.account a WHERE a.accountNumber = :accountNumber")
    Loan findByAccountAccountNumber(@Param("accountNumber") String accountNumber);

}
