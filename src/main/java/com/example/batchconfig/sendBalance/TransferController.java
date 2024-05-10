package com.example.batchconfig.sendBalance;

import com.example.batchconfig.baseResponse.BaseApi;
import com.example.batchconfig.sendBalance.dto.TransferRequest;
import com.example.batchconfig.sendBalance.dto.TransferResponse;
import com.example.batchconfig.sendBalance.dto.WithDrawalRequest;
import com.example.batchconfig.sendBalance.dto.WithdrawalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/sendbalance")
public class TransferController {
    private final TransferService transferService;
    @PostMapping("/transfer")
    public BaseApi<?>registerTransfer(@RequestBody TransferRequest transferRequest) {
       TransferResponse transferResponse= transferService.send(transferRequest);
        return BaseApi.builder()
                .code(HttpStatus.OK.value())
                .message("Account registered successfully")
                .timeStamp(LocalDateTime.now())
                .data(transferResponse)
                .status(true)
                .build();
    }
    @PostMapping("/withdrwal")
    public BaseApi<?>registerWithdrawal(@RequestBody WithDrawalRequest withDrawalRequest) {
        WithdrawalResponse transferResponse= transferService.withdrawalBalance(withDrawalRequest);
        return BaseApi.builder()
                .code(HttpStatus.OK.value())
                .message("Account registered successfully")
                .timeStamp(LocalDateTime.now())
                .data(transferResponse)
                .status(true)
                .build();
    }
}
