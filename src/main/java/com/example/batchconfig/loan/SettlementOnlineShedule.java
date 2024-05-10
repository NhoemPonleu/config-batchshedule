package com.example.batchconfig.loan;

import com.example.batchconfig.brand.brandShedule.BrandTransactionEveryDayDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SettlementOnlineShedule {

    @Autowired
    private LoanService loanService;

 //   @Scheduled(fixedRate = 60000) // Runs every 1 minute
    public void runSettlementAutoLoan() {
        loanService.settleLoansForDepositAccounts();
    }
}

