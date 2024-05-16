package com.example.batchconfig.security.demo;

import com.example.batchconfig.account.Account;
import com.example.batchconfig.account.AccountRepository;
import com.example.batchconfig.account.AccountRequestDTO;
import com.example.batchconfig.account.AccountServiceImpl;
import com.example.batchconfig.account.transaction.AccountTranactionService;
import com.example.batchconfig.account.transaction.AccountWithdrawalResponse;
import com.example.batchconfig.account.transaction.WithdrawalRequestDTO;
import com.example.batchconfig.account.transaction.transfer.TransactionResposnDTO;
import com.example.batchconfig.account.transaction.transfer.TransferRequestDTO;
import com.example.batchconfig.account.transaction.transfer.TransferResponse;
import com.example.batchconfig.baseResponse.BaseApi;
import com.example.batchconfig.exception.ResourceNotFoundException1;
import com.example.batchconfig.util.UserAuthenticationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/interBank")
@Tag(name = "Payment")
@RequiredArgsConstructor
public class ManagementController {


    private final AccountServiceImpl accountService;
    private final AccountTranactionService accountTranactionService;
    private final UserAuthenticationUtils  userAuthenticationUtils;
    private final AccountRepository accountRepository;

    @Operation(
            description = "Get endpoint for manager",
            summary = "This is a summary for management get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }

    )
    @GetMapping
    public String get() {
        return "GET:: management controller";
    }
//    @PostMapping
//    public String post() {
//        return "POST:: management controller";
//    }

    @PostMapping
    public BaseApi<?> registerAccount(@RequestBody AccountRequestDTO accountRequest) {
        Account account = accountService.registerAccount(accountRequest);
        return BaseApi.builder()
                .code(HttpStatus.OK.value())
                .message("Account registered successfully")
                .timeStamp(LocalDateTime.now())
                .data(account)
                .status(true)
                .build();
    }

    //    @PutMapping
//    public String put() {
//        return "PUT:: management controller";
//    }
    @PostMapping("/witdrawal")
    public BaseApi<?> withdrawAccountTransaction(@RequestBody WithdrawalRequestDTO withdrawalRequestDTO) {
        AccountWithdrawalResponse account = accountTranactionService.withdrawAccountTransaction(withdrawalRequestDTO);
        return BaseApi.builder()
                .code(HttpStatus.OK.value())
                .message("Account withdrawal successful")
                .timeStamp(LocalDateTime.now())
                .data(account)
                .status(true)
                .build();
    }

    @PostMapping("/transfer")
    public BaseApi<TransferResponse> transferBalance(@RequestBody TransferRequestDTO transferRequest) {
        TransferResponse transferResponse = accountTranactionService.transferBalance(transferRequest);

        return BaseApi.<TransferResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Transfer successful")
                .timeStamp(LocalDateTime.now())
                .data(transferResponse)
                .status(true)
                .build();
    }
    @GetMapping("/getBalance")
    public BaseApi<?> getAccountBalance() {
        Integer userId = userAuthenticationUtils.getUserRequestDTO().getUserId();
        Account senderAccount = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
        Account sender1=accountRepository.findByAccountNumber(senderAccount.getAccountNumber() )
                .orElseThrow(() -> new ResourceNotFoundException1("Sender account not found","", senderAccount.getAccountNumber()));
        Account transferResponse =accountService.findAccountByAccountNumber(sender1.getAccountNumber());

        return BaseApi.<Account>builder()
                .code(HttpStatus.OK.value())
                .message("Transfer successful")
                .timeStamp(LocalDateTime.now())
                .data(transferResponse)
                .status(true)
                .build();
    }
    @GetMapping("/getTransactions")
    public BaseApi<?> getTransactions() {
        Integer userId = userAuthenticationUtils.getUserRequestDTO().getUserId();
        Account senderAccount = accountRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
        Account sender1=accountRepository.findByAccountNumber(senderAccount.getAccountNumber() )
                .orElseThrow(() -> new ResourceNotFoundException1("Sender account not found","", senderAccount.getAccountNumber()));
   //     Account transferResponse =accountService.findAccountByAccountNumber(sender1.getAccountNumber());

        List<TransactionResposnDTO> transactions = accountService.getTransactions(sender1.getAccountNumber());

        return BaseApi.<List<TransactionResposnDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Transactions retrieved successfully")
                .timeStamp(LocalDateTime.now())
                .data(transactions)
                .status(true)
                .build();
    }

}


//        @DeleteMapping
//        public String delete () {
//            return "DELETE:: management controller";
//        }
