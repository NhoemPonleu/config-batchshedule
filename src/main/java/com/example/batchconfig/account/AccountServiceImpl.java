package com.example.batchconfig.account;

import com.example.batchconfig.account.transaction.AccountTransaction;
import com.example.batchconfig.account.transaction.AccountTransactionRepository;
import com.example.batchconfig.account.transaction.TransactionType;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountTransactionRepository accountTransactionRepository;
    private final AtomicLong accountNumberSequence = new AtomicLong(1L); // Starting sequence number

    @Override
    public Account registerAccount(AccountRequestDTO accountRequestDTO) {
        Account account = new Account();
        String accountNumber = generateAccountNumber(accountRequestDTO.getBrandCode());
        account.setAccountType(accountRequestDTO.getAccountType());
        account.setBalance(accountRequestDTO.getBalance());
        account.setBrandCode(accountRequestDTO.getBrandCode());
        account.setFirstAmount(accountRequestDTO.getBalance());
        account.setLastAmount(accountRequestDTO.getBalance());
        account.setAccountNumber(accountNumber);
        // register in transaction table
        AccountTransaction accountTransaction = new AccountTransaction();
        Long maxSeqNo = accountTransactionRepository.findMaxSeqNoByAccountId(account.getAccountNumber());
        accountTransaction.setTransactionType(TransactionType.FIRST_REGISTER_ACCOUNT.getCode());
        accountTransaction.setTransactionSeqNo(maxSeqNo != null ? maxSeqNo + 1 : 1);
        accountTransaction.setTransactionDate(LocalDate.now());
        accountTransaction.setFirstAmount(accountRequestDTO.getBalance());
        accountTransaction.setAccountId(accountNumber);
        accountTransaction.setTransactionTime(LocalDateTime.now());
        accountTransaction.setAcruedAmount(new BigDecimal(0));
        accountTransaction.setAcruedInterest(new BigDecimal(0));
        accountTransactionRepository.save(accountTransaction);
        return accountRepository.save(account);
    }


    private final ConcurrentHashMap<String, Boolean> accountNumbersMap = new ConcurrentHashMap<>();

    public String generateAccountNumber(String brandCode) {
        String accountNumber;
        do {
            // Increment and get the next sequence number
            long sequenceNumber = accountNumberSequence.getAndIncrement();

            // Combine brand code and sequence number
            accountNumber = brandCode  + String.format("%05d", sequenceNumber);
        } while (accountNumbersMap.putIfAbsent(accountNumber, true) != null); // Check for uniqueness

        return accountNumber;
    }
}
