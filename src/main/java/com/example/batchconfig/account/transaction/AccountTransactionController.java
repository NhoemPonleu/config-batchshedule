package com.example.batchconfig.account.transaction;

import com.example.batchconfig.account.Account;
import com.example.batchconfig.account.transaction.transfer.DepositRequestDTO;
import com.example.batchconfig.account.transaction.transfer.DepositResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/transaction")
@RequiredArgsConstructor
public class AccountTransactionController {

    private final AccountTranactionService accountTransactionService;

    @PostMapping("/withdraw")
    public ResponseEntity<AccountWithdrawalResponse> withdrawAccountTransaction(@RequestBody WithdrawalRequestDTO withdrawalRequestDTO) {
        AccountWithdrawalResponse account = accountTransactionService.withdrawAccountTransaction(withdrawalRequestDTO);
        return ResponseEntity.ok().body(account);
    }
    @PostMapping("/deposit")
    public ResponseEntity<?> depositAccountTransaction(@RequestBody DepositRequestDTO depositRequestDTO) {
        DepositResponseDTO account = accountTransactionService.registerAccountDeposit(depositRequestDTO);
        return ResponseEntity.ok().body(account);
    }
}
