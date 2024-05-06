package com.example.batchconfig.account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService {
    Account registerAccount(AccountRequestDTO accountRequestDTO);
    Account findAccountByAccountNumber(String accountNumber);
    Account findAccountById(Long id);

}
