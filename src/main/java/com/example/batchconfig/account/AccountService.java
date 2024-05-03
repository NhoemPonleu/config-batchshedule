package com.example.batchconfig.account;

import java.math.BigDecimal;

public interface AccountService {
    Account registerAccount(AccountRequestDTO accountRequestDTO);
    Account findAccountByAccountNumber(String accountNumber);

}
