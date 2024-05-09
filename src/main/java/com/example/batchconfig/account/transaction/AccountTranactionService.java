package com.example.batchconfig.account.transaction;

import com.example.batchconfig.account.Account;
import com.example.batchconfig.account.transaction.transfer.*;

public interface AccountTranactionService {
    Long registerAccountTransaction( );
    AccountWithdrawalResponse withdrawAccountTransaction(WithdrawalRequestDTO withdrawalRequestDTO );
    TransferResponse transferBalance(TransferRequestDTO transferRequest);
    Account getAccount(String accountId);
    DepositResponseDTO registerAccountDeposit(DepositRequestDTO depositRequestDTO);
}
