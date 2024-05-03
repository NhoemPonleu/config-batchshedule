package com.example.batchconfig.account.transaction;

import com.example.batchconfig.account.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchAccruredInterest {
    @Autowired
    private  AccountTranactionService accountService;

    @Scheduled(cron = "0 */10 * * * *", zone = "Asia/Phnom_Penh") // run every 10 minutes
    public void accrueInterestDaily() {
        accountService.registerAccountTransaction();
    }
}
