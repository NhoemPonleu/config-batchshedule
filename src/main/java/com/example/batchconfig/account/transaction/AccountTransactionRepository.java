package com.example.batchconfig.account.transaction;

import com.example.batchconfig.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {
    @Query("SELECT COALESCE(MAX(p.transactionSeqNo), 0) FROM AccountTransaction p WHERE p.accountId = :accountId")
    Long findMaxSeqNoByAccountId(@Param("accountId") String accountId);
    List<AccountTransaction> findByAccountId(String accountId);
}
