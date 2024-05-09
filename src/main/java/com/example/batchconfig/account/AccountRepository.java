package com.example.batchconfig.account;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByBalanceGreaterThanEqual(BigDecimal balance);
    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = :balance, a.lastAmount = :lastAmount WHERE a.accountNumber = :accountNumber")
    void updateAmountByAccountNumber(String accountNumber, BigDecimal balance, BigDecimal lastAmount);
    Optional<Account> findByAccountNumber(String accountNumber);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = :balance, a.lastAmount = :lastAmount WHERE a.accountNumber = :accountNumber")
    void updateAccountWithdrawal(String accountNumber, BigDecimal balance, BigDecimal lastAmount);
    Optional<Account> findByUserId(Integer userId);
    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber")
    Account retrieveAccountByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    @Query("SELECT DISTINCT a FROM Account a JOIN FETCH a.loans")
    List<Account> findAllWithLoans();
    @Query("SELECT a.accountNumber FROM Account a WHERE a.customer.id = :customerId")
    List<String> findAccountNumbersByCustomerId(@Param("customerId") Long customerId);


}
