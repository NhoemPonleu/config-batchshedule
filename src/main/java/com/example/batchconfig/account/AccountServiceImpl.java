package com.example.batchconfig.account;

import com.example.batchconfig.account.transaction.AccountTransaction;
import com.example.batchconfig.account.transaction.AccountTransactionRepository;
import com.example.batchconfig.account.transaction.TransactionType;
import com.example.batchconfig.account.transaction.transfer.TransactionResposnDTO;
import com.example.batchconfig.exception.ResourceNotFoundException;
import com.example.batchconfig.exception.ResourceNotFoundException1;
import com.example.batchconfig.security.user.User;
import com.example.batchconfig.security.user.UserRepository;
import com.example.batchconfig.util.UserAuthenticationUtils;
import jakarta.transaction.Transaction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountTransactionRepository accountTransactionRepository;
    private final UserRepository userRepository;
    private final UserAuthenticationUtils userAuthenticationUtils;
    private final AtomicLong accountNumberSequence = new AtomicLong(1L);// Starting sequence number

    @Override
    public Account registerAccount(AccountRequestDTO accountRequestDTO) {
   //    Optional<User> user= userRepository.findById(accountRequestDTO.getUserId());
        Account account = new Account();
        String accountNumber = generateAccountNumber(accountRequestDTO.getBrandCode());
        account.setAccountType(accountRequestDTO.getAccountType());
        account.setBalance(accountRequestDTO.getBalance());
        account.setAccountName(accountRequestDTO.getAccountName());
        account.setBrandCode(accountRequestDTO.getBrandCode());
        account.setFirstAmount(accountRequestDTO.getBalance());
        account.setLastAmount(accountRequestDTO.getBalance());
        account.setUserId(userAuthenticationUtils.getUserRequestDTO().getUserId());
      //  account.setUser(user.get());
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

//    public String generateAccountNumber(String brandCode) {
//        String accountNumber;
//        do {
//            // Increment and get the next sequence number
//            long sequenceNumber = accountNumberSequence.getAndIncrement();
//
//            // Combine brand code and sequence number
//            accountNumber = brandCode  + String.format("%05d", sequenceNumber);
//        } while (accountNumbersMap.putIfAbsent(accountNumber, true) != null); // Check for uniqueness
//
//        return accountNumber;
//    }
public String generateAccountNumber(String brandCode) {
    String accountNumber;
    do {
        // Increment and get the next sequence number
        long sequenceNumber = accountNumberSequence.getAndIncrement();

        // Combine brand code and sequence number
        accountNumber = brandCode + String.format("%05d", sequenceNumber);
    } while (accountRepository.existsByAccountNumber(accountNumber)); // Check for uniqueness in the database

    return accountNumber;
}
    public Account findAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public Account findAccountById(Long id) {
    Account account=  accountRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Account",id));
   return account;
    }
@Override
    public List<TransactionResposnDTO> getTransactions(String accountId) {
        Account account = accountRepository.findByAccountNumber(accountId)
                .orElseThrow(() -> new ResourceNotFoundException1("Account","", accountId));

        List<AccountTransaction> transactions = accountTransactionRepository.findByAccountId(account.getAccountNumber());

        List<TransactionResposnDTO> transactionDTOs = new ArrayList<>();
        for (AccountTransaction transaction : transactions) {
            TransactionResposnDTO transactionDTO = new TransactionResposnDTO();
            transactionDTO.setRecieveFromAccount(transaction.getFromAccountId());
            transactionDTO.setRecieveAmount(transaction.getTransactionAmount());
       //     transactionDTO.setTransaction(transaction.getTransactionType().getDescription()); // Set transaction description
            transactionDTO.setTransactionAmount(transaction.getTransactionTotalAmount());

            LocalDateTime transactionDateTime = transaction.getTransactionTime(); // Assuming you have a LocalDateTime field for transaction date and time

         //   transactionDTO.se(transactionDateTime); // Set transaction date and time
            transactionDTOs.add(transactionDTO);
        }

        return transactionDTOs;
    }

}
