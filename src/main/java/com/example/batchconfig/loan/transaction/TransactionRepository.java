package com.example.batchconfig.loan.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<LoanTransactions, Long> {
    @Query("SELECT COALESCE(MAX(p.transactionSeqNo), 0) FROM LoanTransactions p WHERE p.loanAccountNumber = :loanAccountNumber")
    Long findMaxSeqNoByAccountId(@Param("loanAccountNumber") String loanAccountNumber);
}
