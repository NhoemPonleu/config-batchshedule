package com.example.batchconfig.account.transaction;

import com.example.batchconfig.account.Account;
import com.example.batchconfig.account.AccountRepository;
import com.example.batchconfig.brand.brandShedule.TellerTypeCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountTransactionServiceImpl implements AccountTranactionService {
    private static final Logger logger = LoggerFactory.getLogger(AccountTransactionServiceImpl.class);

    private final AccountTransactionRepository accountTransactionRepository;
    private final AccountRepository accountRepository;
    private static final BigDecimal INTEREST_RATE = new BigDecimal("0.02"); // 2% interest rate

    @Override
    public void registerAccountTransaction() {
        // find account balance>1000
        List<Account> accounts = accountRepository.findByBalanceGreaterThanEqual(new BigDecimal("1000"));
        long size=accounts.size();
        if (accounts.isEmpty()) {
            logger.warn("No accounts with a balance greater than or equal to $1000 found.");
            throw new RuntimeException("No accounts with a balance greater than or equal to $1000 found.");
        }
        LocalDate today = LocalDate.now();
        for (Account account : accounts) {
            BigDecimal balance = account.getBalance();
            BigDecimal interest = balance.multiply(INTEREST_RATE);
            BigDecimal newBalance = balance.add(interest);

            Long maxSeqNo = accountTransactionRepository.findMaxSeqNoByAccountId(account.getAccountNumber());
            AccountTransaction accountTransaction1 = new AccountTransaction();
            accountTransaction1.setAccountId(account.getAccountNumber());
            accountTransaction1.setTransactionDate(LocalDate.now());
            accountTransaction1.setFirstAmount(balance);
            accountTransaction1.setAcruedAmount(newBalance);
            accountTransaction1.setAcruedInterest(interest);
            accountTransaction1.setTransactionSeqNo(maxSeqNo != null ? maxSeqNo + 1 : 1);
            accountTransaction1.setTransactionType(TellerTypeCode.BATCH_TELLER.getCode());
            accountTransaction1.setTransactionTime(LocalDateTime.now());
            accountTransaction1.setRegisterTime(LocalTime.now());
            //register into table transaction
            accountTransactionRepository.save(accountTransaction1);

            //update into master table
            accountRepository.updateAmountByAccountNumber(account.getAccountNumber(), newBalance, newBalance);
        }
    }
}
