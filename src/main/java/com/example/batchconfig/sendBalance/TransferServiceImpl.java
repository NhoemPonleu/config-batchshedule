package com.example.batchconfig.sendBalance;

import com.example.batchconfig.account.Account;
import com.example.batchconfig.account.AccountRepository;
import com.example.batchconfig.account.AccountService;
import com.example.batchconfig.account.transaction.AccountTransactionRepository;
import com.example.batchconfig.exception.InsufficientBalanceException;
import com.example.batchconfig.sendBalance.constand.*;
import com.example.batchconfig.sendBalance.dto.TransferRequest;
import com.example.batchconfig.sendBalance.dto.TransferResponse;
import com.example.batchconfig.sendBalance.dto.WithDrawalRequest;
import com.example.batchconfig.sendBalance.dto.WithdrawalResponse;
import com.example.batchconfig.util.UserAuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Currency;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    Logger logger = Logger.getLogger(this.getClass().getName());
    // Get the current stack trace
    StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

    // The method name is the second element in the stack trace
    String methodName = stackTraceElements[2].getMethodName();
    private final UserAuthenticationUtils userAuthenticationUtils;
    private final TransferRepository transferRepository;
    private final TransactionDetailsRepository transactionDetailsRepository;
    private final GeneratePasswordSenderUtil generateRandomPassword;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    @Override
    public TransferResponse send(TransferRequest transferRequest) {
        Account recipientAccount = null;
        if (!SendTypeCode.NORMAL.getCode().equals(transferRequest.getSendType())) {
            recipientAccount = accountService.findAccountByAccountNumber(transferRequest.getToAccountNumber());
        }
        Integer password = generateRandomPassword.generateRandomPassword();

        Transfer transfer = createTransfer(transferRequest, recipientAccount, password);
        if (recipientAccount != null) {
            updateRecipientAccountBalance(recipientAccount, transferRequest.getTransferAmount());
        }

        BigDecimal feeAmount = calculateFee(transferRequest, transfer);
        transfer.setFeeAmount(feeAmount);

        transferRepository.save(transfer);

        return createTransferResponse(transferRequest, transfer, password, feeAmount);
    }

    private Transfer createTransfer(TransferRequest transferRequest, Account recipientAccount, Integer password) {
        Transfer transfer = new Transfer();
        transfer.setTransferDate(LocalDate.now());
        transfer.setTransferTime(LocalTime.now());
        transfer.setAfterTransferAmount(transferRequest.getTransferAmount());
        transfer.setPassword(password);
        transfer.setUuidPassword(UUID.randomUUID());
        transfer.setTransferStatus(TransferTypeCode.NORMAL.getCode());

        if (SendTypeCode.TO_ACCOUNT.getCode().equals(transferRequest.getSendType())) {
            transfer.setToAccountNumber(transferRequest.getToAccountNumber());
            transfer.setToAccountName(recipientAccount != null ? recipientAccount.getAccountName() : null);
            transfer.setWithdrawalYN("Y");
        } else if (SendTypeCode.NORMAL.getCode().equals(transferRequest.getSendType())) {
            transfer.setSenderPhoneNumber(transferRequest.getSenderPhoneNumber());
            transfer.setReceiverPhoneNumber(transferRequest.getReceiverPhoneNumber());
            transfer.setToAccountNumber(null);
            transfer.setToAccountName(null);
            transfer.setWithdrawalYN("N");
        } else {
            throw new IllegalArgumentException("Unsupported send type: " + transferRequest.getSendType());
        }

        transfer.setUserRegister(userAuthenticationUtils.getUserRequestDTO().getUserId());

        return transfer;
    }

    private void updateRecipientAccountBalance(Account recipientAccount, BigDecimal transferAmount) {
        BigDecimal newBalance = recipientAccount.getBalance().add(transferAmount);
        recipientAccount.setBalance(newBalance);
        accountRepository.save(recipientAccount);
    }

    private BigDecimal calculateFee(TransferRequest transferRequest, Transfer transfer) {
        BigDecimal feeAmount = BigDecimal.ZERO;

        if (FeeTypeCode.RECEIVER.getCode().equals(transferRequest.getFeeTypeCode())) {
            feeAmount = calculateReceiverFee(transferRequest);
            transfer.setFeeTypeCode(FeeTypeCode.RECEIVER);
            BigDecimal afterFee = transferRequest.getTransferAmount().subtract(feeAmount);
            transfer.setTransferAmount(afterFee);
        } else if (FeeTypeCode.SENDER.getCode().equals(transferRequest.getFeeTypeCode())) {
            feeAmount = validateAndRetrieveSenderFee(transferRequest);
            transfer.setFeeTypeCode(FeeTypeCode.SENDER);
            transfer.setTransferAmount(transferRequest.getTransferAmount());
        }

        return feeAmount;
    }

    private BigDecimal calculateReceiverFee(TransferRequest transferRequest) {
        BigDecimal transferAmount = transferRequest.getTransferAmount();
        if (transferAmount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            return transferAmount.multiply(BigDecimal.valueOf(0.02)); // 2% fee
        } else {
            return transferAmount.multiply(BigDecimal.valueOf(0.01)); // 1% fee
        }
    }

    private BigDecimal validateAndRetrieveSenderFee(TransferRequest transferRequest) {
        BigDecimal feeAmount = transferRequest.getFeeAmount();
        if (feeAmount == null || feeAmount.compareTo(BigDecimal.ZERO) <= 0) {
            String errorMsg = "Fee amount cannot be null or zero for sender fee type";
            logger.warning(errorMsg);
            throw new CheckStatuErrorCode(errorMsg, transferRequest.getFeeTypeCode());
        }
        return feeAmount;
    }

    private TransferResponse createTransferResponse(TransferRequest transferRequest, Transfer transfer, Integer password, BigDecimal feeAmount) {
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setTransferTime(LocalTime.now());
        transferResponse.setSenderPhoneNumber(transferRequest.getSenderPhoneNumber());
        transferResponse.setReceiverPhoneNumber(transferRequest.getReceiverPhoneNumber());
        transferResponse.setTransferAmount(transferRequest.getTransferAmount());
        transferResponse.setSecureNumber(password);
        transferResponse.setFeeAmount(feeAmount);
        transferResponse.setTransferDate(LocalDate.now());
        transferResponse.setUserRequestDTO(userAuthenticationUtils.getUserRequestDTO());

        return transferResponse;
    }

    @Override
    public WithdrawalResponse withdrawalBalance(WithDrawalRequest withdrawalRequest) {
        // Find the transfer by receiver phone number, withdrawal amount, and password
        Transfer transfer = transferRepository.findByReceiverPhoneNumberAndPassword(
                withdrawalRequest.getReciverPhoneNumber(),
                withdrawalRequest.getRequestPassword());
            if(!transfer.getPassword().equals(withdrawalRequest.getRequestPassword())&&
                    !transfer.getReceiverPhoneNumber().equals(withdrawalRequest.getReciverPhoneNumber())) {
                throw new InsufficientBalanceException("Password and receiver phone number are not match!!!");
            }

        if (transfer.getWithdrawalYN().equals("N")) {
            if (transfer.getTransferAmount().compareTo(withdrawalRequest.getWithdrawalAmount()) != 0) {
                throw new InsufficientBalanceException("Transfer amount does not match withdrawal amount");
            }
            // Perform withdrawal
            // Your withdrawal logic goes here

            // Update the transfer status
            transfer.setWithdrawalYN("Y");
            transfer.setAfterTransferAmount(transfer.getTransferAmount().subtract(withdrawalRequest.getWithdrawalAmount()));
            transferRepository.save(transfer);
            // when withdrwal balance register into table transaction details
            TransactionDetails transactionDetails = new TransactionDetails();
            transactionDetails.setTransactionAmount(withdrawalRequest.getWithdrawalAmount());
            transactionDetails.setTransactionDate(LocalDate.now());
            transactionDetails.setTransactionTime(LocalTime.now());
            transactionDetails.setReceiverPhoneNumber(transfer.getReceiverPhoneNumber());
            transactionDetails.setFeeAmount(transfer.getFeeAmount());
            transactionDetails.setTransactionTotalAmount(transfer.getTransferAmount().add(transfer.getFeeAmount()));
            transactionDetails.setTransactionType(WithdrawalTransferTypeCode.WITHDRAWAL);
            transactionDetails.setSenderPhoneNumber(transfer.getSenderPhoneNumber());
            transactionDetailsRepository.save(transactionDetails);
            // Construct withdrawal response
            WithdrawalResponse withdrawalResponse = new WithdrawalResponse();
            withdrawalResponse.setWithdrawalTime(LocalTime.now());
            withdrawalResponse.setReciverPhoneNumber(withdrawalRequest.getReciverPhoneNumber());
            withdrawalResponse.setWithdrawalAmount(withdrawalRequest.getWithdrawalAmount());
            withdrawalResponse.setWithdrawalDate(LocalDate.now());
            withdrawalResponse.setTransactionTypeCode(WithdrawalTransferTypeCode.WITHDRAWAL.getDescription());
            withdrawalResponse.setUserRequestDTO(userAuthenticationUtils.getUserRequestDTO());

            return withdrawalResponse;
        } else {
            // If no valid transfer found or withdrawal already done, return error response
            throw new CheckStatuErrorCode("Invalid withdrawal request", withdrawalRequest.getReciverPhoneNumber());
        }
    }

}




