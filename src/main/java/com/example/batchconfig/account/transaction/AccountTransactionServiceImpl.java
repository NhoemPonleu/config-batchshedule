package com.example.batchconfig.account.transaction;

import com.example.batchconfig.account.Account;
import com.example.batchconfig.account.AccountRepository;
import com.example.batchconfig.account.AccountService;
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
    private final AccountService accountService;
    private static final BigDecimal INTEREST_RATE = new BigDecimal("0.02"); // 2% interest rate

    @Override
    public Long registerAccountTransaction() {
        // find accounts with balance > 1000
        List<Account> accounts = accountRepository.findByBalanceGreaterThanEqual(new BigDecimal("1000"));

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

            // register into transaction table
            accountTransactionRepository.save(accountTransaction1);

            // update into master table
            accountRepository.updateAmountByAccountNumber(account.getAccountNumber(), newBalance, newBalance);
        }

        // Count total accounts with balance > 1000
        long count = accounts.stream()
                .filter(account -> account.getBalance().compareTo(new BigDecimal("1000")) > 0)
                .count();

        return count;
    }

    @Override
    public AccountWithdrawalResponse withdrawAccountTransaction(WithdrawalRequestDTO withdrawalRequestDTO) {
        // Find the account by its account number
        Account account = accountService.findAccountByAccountNumber(withdrawalRequestDTO.getAccountId());
          AccountWithdrawalResponse accountWithdrawalResponse = new AccountWithdrawalResponse();
        AccountWithdrawalResponse response = new AccountWithdrawalResponse();
        response.setAccountId(account.getAccountNumber());
        response.setTransactionAmount(withdrawalRequestDTO.getTransactionAmount());
        response.setAccountName(account.getAccountType());
        response.setTransactionDate(LocalDate.now());
        response.setTransactionTime(LocalTime.now());
        // Check if the withdrawal amount is valid
        BigDecimal withdrawalAmount = withdrawalRequestDTO.getTransactionAmount();
        if (withdrawalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        if (withdrawalAmount.compareTo(account.getBalance()) > 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        // Create a new account transaction record
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setAccountId(account.getAccountNumber());
        accountTransaction.setTransactionDate(LocalDate.now());
        accountTransaction.setFirstAmount(withdrawalAmount.negate()); // Negate the amount for withdrawal

        // Save the transaction record
        accountTransactionRepository.save(accountTransaction);

        // Update the account balance
        BigDecimal newBalance = account.getBalance().subtract(withdrawalAmount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        return response;
    }

}

