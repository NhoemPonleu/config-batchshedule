package com.example.batchconfig.security.demo;

import com.example.batchconfig.account.Account;
import com.example.batchconfig.account.AccountRequestDTO;
import com.example.batchconfig.account.AccountServiceImpl;
import com.example.batchconfig.baseResponse.BaseApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/management")
@Tag(name = "Management")
@RequiredArgsConstructor
public class ManagementController {


    private final AccountServiceImpl accountService;
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
        Account account= accountService.registerAccount(accountRequest);
        return BaseApi.builder()
                .code(HttpStatus.OK.value())
                .message("Account registered successfully")
                .timeStamp(LocalDateTime.now())
                .data(account)
                .status(true)
                .build();
    }
    @PutMapping
    public String put() {
        return "PUT:: management controller";
    }
    @DeleteMapping
    public String delete() {
        return "DELETE:: management controller";
    }
}
