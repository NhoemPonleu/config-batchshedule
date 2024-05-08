package com.example.batchconfig.account;

import com.example.batchconfig.account.transaction.transfer.TransactionResposnDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    Account registerAccount(AccountRequestDTO accountRequestDTO);
    Account findAccountByAccountNumber(String accountNumber);
    Account findAccountById(Long id);
    List<TransactionResposnDTO> getTransactions(String accountId);

}
