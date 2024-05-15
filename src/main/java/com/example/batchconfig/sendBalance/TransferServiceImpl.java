package com.example.batchconfig.sendBalance;

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
    @Override
    public TransferResponse send(TransferRequest transferRequest) {
        Integer password=generateRandomPassword.generateRandomPassword();
        Transfer transfer = new Transfer();
        transfer.setTransferDate(LocalDate.now());
      //  transfer.setTransferAmount(transferRequest.getTransferAmount());
        transfer.setTransferTime(LocalTime.now());
        transfer.setSenderPhoneNumber(transferRequest.getSenderPhoneNumber());
        transfer.setReceiverPhoneNumber(transferRequest.getReceiverPhoneNumber());
        transfer.setAfterTransferAmount(transferRequest.getTransferAmount());
        transfer.setPassword(password);
        transfer.setUuidPassword(UUID.randomUUID());
        transfer.setTransferStatus(TransferTypeCode.NORMAL.getCode());
        transfer.setWithdrawalYN("N");

        BigDecimal feeAmount = BigDecimal.ZERO ;
        if (transferRequest.getFeeTypeCode().equals(FeeTypeCode.RECEIVER.getCode())) {
            if (transferRequest.getTransferAmount().compareTo(BigDecimal.valueOf(1000)) > 0) {
                feeAmount = transferRequest.getTransferAmount().multiply(BigDecimal.valueOf(0.02)); // 2% fee
            } else {
                feeAmount = transferRequest.getTransferAmount().multiply(BigDecimal.valueOf(0.01)); // 1% fee
            }
            transfer.setFeeTypeCode(FeeTypeCode.RECEIVER);
            BigDecimal afterFee = transferRequest.getTransferAmount().subtract(feeAmount);
            transfer.setTransferAmount(afterFee);
        } else if (transferRequest.getFeeTypeCode().equals(FeeTypeCode.SENDER.getCode())) {
            feeAmount = transferRequest.getFeeAmount();
            if (feeAmount == null || feeAmount.compareTo(BigDecimal.ZERO) <= 0) {
                logger.warning("Fee amount cannot be null or zero for sender fee type");
                logger.info("Current method: " + methodName);
                throw new CheckStatuErrorCode("Fee amount cannot be null or zero for sender fee type", transferRequest.getFeeTypeCode());
            }
            transfer.setFeeTypeCode(FeeTypeCode.SENDER);
            BigDecimal afterTransferAmount = transferRequest.getTransferAmount();
            transfer.setTransferAmount(afterTransferAmount);
        }
        transfer.setUserRegister(userAuthenticationUtils.getUserRequestDTO().getUserId());
        transfer.setFeeAmount(feeAmount);
        transferRepository.save(transfer);
        // response to client
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




