package com.example.batchconfig.loan.transaction;

import com.example.batchconfig.loan.AccountDeposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDepositRepository extends JpaRepository<AccountDeposit, Long> {
}
