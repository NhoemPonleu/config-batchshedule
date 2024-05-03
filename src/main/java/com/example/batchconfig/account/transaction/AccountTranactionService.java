package com.example.batchconfig.account.transaction;

import com.example.batchconfig.account.Account;

public interface AccountTranactionService {
    Long registerAccountTransaction( );
    AccountWithdrawalResponse withdrawAccountTransaction(WithdrawalRequestDTO withdrawalRequestDTO );
}
