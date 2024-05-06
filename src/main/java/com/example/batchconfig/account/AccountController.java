package com.example.batchconfig.account;

import com.example.batchconfig.baseResponse.BaseApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

//    @PostMapping
//    public BaseApi<?> registerAccount(@RequestBody AccountRequestDTO accountRequest) {
//       Account account= accountService.registerAccount(accountRequest);
//        return BaseApi.builder()
//                .code(HttpStatus.OK.value())
//                .message("Account registered successfully")
//                .timeStamp(LocalDateTime.now())
//                .data(account)
//                .status(true)
//                .build();
//    }
    @GetMapping
    public String helloworld(){
        return "Hello World";
    }
}
