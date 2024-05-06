package com.example.batchconfig.loan.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<LoanTransactions, Long> {
}
