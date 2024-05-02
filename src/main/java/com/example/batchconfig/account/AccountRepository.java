package com.example.batchconfig.account;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByBalanceGreaterThanEqual(BigDecimal balance);
    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = :balance, a.lastAmount = :lastAmount WHERE a.accountNumber = :accountNumber")
    void updateAmountByAccountNumber(String accountNumber, BigDecimal balance, BigDecimal lastAmount);

}