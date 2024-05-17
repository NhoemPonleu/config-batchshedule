package com.example.batchconfig.account.transaction;

import com.example.batchconfig.account.Account;
import com.example.batchconfig.account.AccountRepository;
import com.example.batchconfig.account.AccountService;
import com.example.batchconfig.account.transaction.transfer.DepositRequestDTO;
import com.example.batchconfig.account.transaction.transfer.DepositResponseDTO;
import com.example.batchconfig.account.transaction.transfer.TransferRequestDTO;
import com.example.batchconfig.account.transaction.transfer.TransferResponse;
import com.example.batchconfig.brand.brandShedule.TellerTypeCode;
import com.example.batchconfig.errorException.MRegisterErrException;
import com.example.batchconfig.exception.CheckStatuError;
import com.example.batchconfig.exception.InsufficientBalanceException;
import com.example.batchconfig.exception.ResourceNotFoundException;
import com.example.batchconfig.exception.ResourceNotFoundException1;
import com.example.batchconfig.util.UserAuthenticationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final UserAuthenticationUtils userAuthenticationUtils;

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
    private Account validationAccountWithdrwal(WithdrawalRequestDTO withdrawalRequestDTO){
        // Find the account by its account number
        Account account = accountService.findAccountByAccountNumber(withdrawalRequestDTO.getAccountId());
        if(account.getBalance().compareTo(withdrawalRequestDTO.getTransactionAmount())<0){
            logger.warn("Insufficient balance for withdrawal: " + withdrawalRequestDTO.getTransactionAmount());
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }
        if(withdrawalRequestDTO.getTransactionAmount().compareTo(new BigDecimal(1000)) > 0){
            logger.warn("withdrawal balance for withdrawal cannot bigger than 1000$");
            throw new InsufficientBalanceException("Withdrwal Balance Cannot Bigger Than 1000$");
        }
        return account;
    }
    @Override
    public AccountWithdrawalResponse withdrawAccountTransaction(WithdrawalRequestDTO withdrawalRequestDTO) {
        Account account= validationAccountWithdrwal(withdrawalRequestDTO);
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
        try {
            Integer userId = userAuthenticationUtils.getUserRequestDTO().getUserId();
            Account senderAccount = accountRepository.findByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
            Account sender1=accountRepository.findByAccountNumber(senderAccount.getAccountNumber() )
                    .orElseThrow(() -> new ResourceNotFoundException1("Sender account not found","", senderAccount.getAccountNumber()));
            Account receiverAccount = accountRepository.findByAccountNumber(transferRequest.getReceiverAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

            BigDecimal amount = transferRequest.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Transfer amount must be greater than zero");
            }

            BigDecimal senderNewBalance = senderAccount.getBalance().subtract(amount);
            if (senderNewBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new InsufficientBalanceException("Insufficient funds in sender's account");
            }
            BigDecimal receiverNewBalance = receiverAccount.getBalance().add(amount);

            senderAccount.setBalance(senderNewBalance);
            receiverAccount.setBalance(receiverNewBalance);

            accountRepository.save(senderAccount);
            accountRepository.save(receiverAccount);

            AccountTransaction accountTransaction = new AccountTransaction();
            accountTransaction.setAccountId(senderAccount.getAccountNumber());
            accountTransaction.setTransactionAmount(amount);
            accountTransaction.setTransactionDate(LocalDate.now());
            accountTransaction.setTransactionType(TransactionType.TRANSFER_ACCOUNT.getCode());
            accountTransaction.setTransactionTotalAmount(amount);
            accountTransaction.setFromAccountId(senderAccount.getAccountNumber());
            accountTransaction.setToAccountId(receiverAccount.getAccountNumber());
            accountTransactionRepository.save(accountTransaction);

            TransferResponse transferResponse = new TransferResponse();
            transferResponse.setTransactionDate(LocalDate.now());
            transferResponse.setAccountSenderName(senderAccount.getAccountName());
            transferResponse.setAccountReceiverName(receiverAccount.getAccountName());
            transferResponse.setReceiverAccountId(receiverAccount.getAccountNumber());
            transferResponse.setSenderAccountId(senderAccount.getAccountNumber());
            transferResponse.setTransactionTime(LocalTime.now());
            transferResponse.setAmount(amount);
            transferResponse.setTransactionTotalAmount(amount);

            return transferResponse;
        } catch (IllegalArgumentException | ResourceNotFoundException | InsufficientBalanceException e) {
            throw e; // Rethrow known exceptions
        } catch (Exception e) {
            e.printStackTrace();
            throw new MRegisterErrException("Transfer Error");
        }
    }


    @Override
    public Account getAccount(String accountId) {
        Integer userId = userAuthenticationUtils.getUserRequestDTO().getUserId();
        Account senderAccount = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
        Account sender1=accountRepository.findByAccountNumber(senderAccount.getAccountNumber() )
                .orElseThrow(() -> new ResourceNotFoundException1("Sender account not found","", senderAccount.getAccountNumber()));
        return accountRepository.retrieveAccountByAccountNumber(sender1.getAccountNumber());
    }

    @Override
    public DepositResponseDTO registerAccountDeposit(DepositRequestDTO depositRequestDTO) {
        Integer userId = userAuthenticationUtils.getUserRequestDTO().getUserId();
        Account senderAccount = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
        Account sender1=accountRepository.findByAccountNumber(senderAccount.getAccountNumber() )
                .orElseThrow(() -> new ResourceNotFoundException1("Sender account not found","", senderAccount.getAccountNumber()));
        if (!"Y".equals(sender1.getAccountStatusYN())) {
            logger.warn("Account status is not normal");
            throw new CheckStatuError("Account not normal", "ResourceName", sender1.getAccountNumber());
        }
        AccountTransaction accountTransaction = new AccountTransaction();
        accountTransaction.setAccountId(sender1.getAccountNumber());
        accountTransaction.setTransactionAmount(depositRequestDTO.getDepositAmount());
        accountTransaction.setTransactionDate(LocalDate.now());
        accountTransaction.setTransactionType(TransactionType.DEPOSIT_ACCOUNT.getCode());
        accountTransaction.setTransactionTotalAmount(depositRequestDTO.getDepositAmount());
        accountTransactionRepository.save(accountTransaction);
        BigDecimal newBalance = sender1.getBalance().add(depositRequestDTO.getDepositAmount());
        sender1.setBalance(newBalance);
        accountRepository.save(sender1);
        // Log transaction details
        logger.warn("Transaction registered successfully: " +
                "AccountId: " + accountTransaction.getAccountId() + ", " +
                "TransactionAmount: " + accountTransaction.getTransactionAmount() + ", " +
                "TransactionDate: " + accountTransaction.getTransactionDate() + ", " +
                "TransactionType: " + accountTransaction.getTransactionType() + ", " +
                "TransactionTotalAmount: " + accountTransaction.getTransactionTotalAmount());
        DepositResponseDTO  depositResponseDTO = new DepositResponseDTO();
        depositResponseDTO.setTransactionDate(LocalDate.now());
        depositResponseDTO.setDepositAmount(depositResponseDTO.getDepositAmount());
        depositResponseDTO.setTransactionType(TransactionType.DEPOSIT_ACCOUNT.getDescription());
        depositResponseDTO.setTransactionDate(LocalDate.now());
        depositResponseDTO.setTransactionTime(LocalTime.now());
        return depositResponseDTO;
    }


}

