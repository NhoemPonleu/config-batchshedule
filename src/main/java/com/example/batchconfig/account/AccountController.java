package com.example.batchconfig.account;

import com.example.batchconfig.baseResponse.BaseApi;
import com.example.batchconfig.exception.ResourceNotFoundException1;
import com.example.batchconfig.util.UserAuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final UserAuthenticationUtils userAuthenticationUtils;
    private final AccountRepository accountRepository;
    @GetMapping("/{accountId}")
    public BaseApi<?> registerAccount(@PathVariable Long accountId) {
        Account account= accountService.findAccountById(accountId);
        return BaseApi.builder()
                .code(HttpStatus.OK.value())
                .message("Account registered successfully")
                .timeStamp(LocalDateTime.now())
                .data(account)
                .status(true)
                .build();
    }
    @GetMapping
    public String helloworld(){
        return "Hello World";
    }
}
