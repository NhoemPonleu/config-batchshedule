package com.example.batchconfig.account.transaction;

import com.example.batchconfig.account.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BatchAccruredInterest {
    @Autowired
    private  AccountTranactionService accountService;

    @Scheduled(cron = "0 0 */3 * * *", zone = "Asia/Phnom_Penh")// run at midnight every day
    public void accrueInterestDaily() {
        accountService.registerAccountTransaction();
    }
}
