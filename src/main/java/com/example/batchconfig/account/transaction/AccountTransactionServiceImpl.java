package com.example.batchconfig.account.transaction;

import com.example.batchconfig.account.Account;
import com.example.batchconfig.account.AccountRepository;
import com.example.batchconfig.account.AccountService;
import com.example.batchconfig.account.transaction.transfer.TransferRequestDTO;
import com.example.batchconfig.account.transaction.transfer.TransferResponse;
import com.example.batchconfig.brand.brandShedule.TellerTypeCode;
import jakarta.transaction.Transactional;
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
        Long maxSeqNo = accountTransactionRepository.findMaxSeqNoByAccountId(account.getAccountNumber());
        // Create a new account transaction record
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setTransactionSeqNo(maxSeqNo != null ? maxSeqNo + 1 : 1);
        accountTransaction.setAccountId(account.getAccountNumber());
        accountTransaction.setTransactionDate(LocalDate.now());
        accountTransaction.setFirstAmount(withdrawalAmount.negate()); // Negate the amount for withdrawal
        accountTransaction.setTransactionType(TransactionType.WITHDRAW_ACCOUNT.getCode());
        accountTransaction.setTransactionTime(LocalDateTime.now());
        accountTransaction.setRegisterTime(LocalTime.now());
        // Save the transaction record
        accountTransactionRepository.save(accountTransaction);

        // Update the account balance
        BigDecimal newBalance = account.getBalance().subtract(withdrawalAmount);
        account.setBalance(newBalance);
        accountRepository.save(account);

        return response;
    }
    @Transactional
    public TransferResponse transferBalance(TransferRequestDTO transferRequest) {
        Account senderAccount = accountRepository.findByAccountNumber(transferRequest.getSenderAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));

        Account receiverAccount = accountRepository.findByAccountNumber(transferRequest.getReceiverAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

        BigDecimal amount = transferRequest.getAmount();

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        if (senderAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance in sender account");
        }

        BigDecimal senderNewBalance = senderAccount.getBalance().subtract(amount);
        BigDecimal receiverNewBalance = receiverAccount.getBalance().add(amount);

        senderAccount.setBalance(senderNewBalance);
        receiverAccount.setBalance(receiverNewBalance);
     //   Long maxSeqNo = accountTransactionRepository.findMaxSeqNoByAccountId(account.getAccountNumber());
        // register into table transaction
        AccountTransaction accountTransaction=new AccountTransaction();
        accountTransaction.setAccountId(transferRequest.getSenderAccountId());
        accountTransaction.setAccountId(transferRequest.getReceiverAccountId());
        accountTransaction.setTransactionAmount(transferRequest.getAmount());
        accountTransaction.setTransactionDate(LocalDate.now());
        accountTransaction.setTransactionType(TransactionType.TRANSFER_ACCOUNT.getCode());
        accountTransaction.setTransactionTotalAmount(transferRequest.getAmount());
        accountTransaction.setFromAccountId(transferRequest.getSenderAccountId());
        accountTransaction.setToAccountId(transferRequest.getReceiverAccountId());
        accountTransactionRepository.save(accountTransaction);

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // return reponse data to client
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setTransactionDate(LocalDate.now());
        transferResponse.setReceiverAccountId(transferRequest.getReceiverAccountId());
        transferResponse.setSenderAccountId(transferRequest.getSenderAccountId());
        transferResponse.setTransactionTime(LocalTime.now());
        transferResponse.setAmount(amount);
    //    transferResponse.setTransactionTotalAmount(amount.add(transferRequest.));
        transferResponse.setTransactionTotalAmount(amount);

        return transferResponse;
    }

}

