package com.example.batchconfig.account.transaction;

import com.example.batchconfig.account.Account;
import com.example.batchconfig.account.transaction.transfer.TransactionResposnDTO;
import com.example.batchconfig.account.transaction.transfer.TransferRequestDTO;
import com.example.batchconfig.account.transaction.transfer.TransferResponse;

import java.util.List;

public interface AccountTranactionService {
    Long registerAccountTransaction( );
    AccountWithdrawalResponse withdrawAccountTransaction(WithdrawalRequestDTO withdrawalRequestDTO );
    TransferResponse transferBalance(TransferRequestDTO transferRequest);
    Account getAccount(String accountId);
}
