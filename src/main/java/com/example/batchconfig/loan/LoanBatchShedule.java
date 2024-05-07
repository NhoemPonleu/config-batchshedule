package com.example.batchconfig.loan;

import com.example.batchconfig.account.transaction.AccountTranactionService;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LoanBatchShedule {
    @Autowired
    private LoanService loanService;

    @Scheduled(cron = "0 */1 * * * *", zone = "Asia/Phnom_Penh")
    public void accrueInterestDaily() {
        loanService.accruedInterestEveryday();
    }
}
