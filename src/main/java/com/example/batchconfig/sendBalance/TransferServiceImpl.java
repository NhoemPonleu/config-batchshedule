package com.example.batchconfig.sendBalance;

import com.example.batchconfig.exception.InsufficientBalanceException;
import com.example.batchconfig.sendBalance.constand.CheckStatuErrorCode;
import com.example.batchconfig.sendBalance.constand.FeeTypeCode;
import com.example.batchconfig.sendBalance.constand.TransferTypeCode;
import com.example.batchconfig.sendBalance.constand.WithdrawalTransferTypeCode;
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
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private final UserAuthenticationUtils userAuthenticationUtils;
    private final TransferRepository transferRepository;
    private final TransactionDetailsRepository transactionDetailsRepository;
    @Override
    public TransferResponse send(TransferRequest transferRequest) {
        Integer password=generateRandomPassword();
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

        BigDecimal feeAmount = BigDecimal.ZERO;
        if (transferRequest.getFeeTypeCode().equals(FeeTypeCode.RECEIVER.getCode()) && transferRequest.getTransferAmount().compareTo(BigDecimal.valueOf(1000)) > 0) {
            feeAmount = transferRequest.getTransferAmount().multiply(BigDecimal.valueOf(0.02)); // 2% fee
            transfer.setFeeTypeCode(FeeTypeCode.RECEIVER);
            BigDecimal afterFee=transferRequest.getTransferAmount().subtract(feeAmount);
            transfer.setTransferAmount(afterFee);
        } else if (transferRequest.getFeeTypeCode().equals(FeeTypeCode.SENDER.getCode())) {
            feeAmount=transferRequest.getFeeAmount();
            transfer.setFeeTypeCode(FeeTypeCode.SENDER);
            transfer.setTransferAmount(transferRequest.getTransferAmount());
        }
        transfer.setFeeAmount(feeAmount);
        // response to client
        TransferResponse transferResponse = new TransferResponse();
        transferResponse.setTransferTime(LocalTime.now());
        transferResponse.setSenderPhoneNumber(transferRequest.getSenderPhoneNumber());
        transferResponse.setReceiverPhoneNumber(transferRequest.getReceiverPhoneNumber());
        transferResponse.setTransferAmount(transferRequest.getTransferAmount());
        transferResponse.setFeeAmount(feeAmount);
        transferResponse.setTransferDate(LocalDate.now());
        transferResponse.setUserRequestDTO(userAuthenticationUtils.getUserRequestDTO());
        transfer.setUserRegister(userAuthenticationUtils.getUserRequestDTO().getUserId());
        transferRepository.save(transfer);

        return transferResponse;
    }

    // Method to generate a random password
    private int generateRandomPassword() {
        Random random = new Random();
        return 100000 + random.nextInt(900000); // Generates a random 6-digit number
    }
    @Override
    public WithdrawalResponse withdrawalBalance(WithDrawalRequest withdrawalRequest) {
        // Find the transfer by receiver phone number, withdrawal amount, and password
        Transfer transfer = transferRepository.findByReceiverPhoneNumberAndPassword(
                withdrawalRequest.getReciverPhoneNumber(),
                withdrawalRequest.getRequestPassword());

        if (transfer != null && transfer.getWithdrawalYN().equals("N")) {
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




