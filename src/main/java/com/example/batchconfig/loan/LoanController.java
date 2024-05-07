package com.example.batchconfig.loan;

import com.example.batchconfig.baseResponse.BaseApi;
import com.example.batchconfig.loan.transaction.GenerateScheduleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/loan")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping
    public BaseApi<?>registerNewLoan(@RequestBody LoanRequestDTO loanRequestDTO) {
       LoanReposeDTO loanReposeDTO= loanService.registerNewLoan(loanRequestDTO);
                return BaseApi.builder()
                .code(HttpStatus.OK.value())
                .message("Loan registered successfully")
                .timeStamp(LocalDateTime.now())
                .data(loanReposeDTO)
                .status(true)
                .build();
    }
    @PostMapping("/shedule")
    public BaseApi<?>registerShedulePayment(RequestSheduleDTO sheduleDTO) {
        GenerateScheduleDTO loanScheduleItem=loanService.generateLoanSchedule(sheduleDTO);
        return BaseApi.builder()
                .code(HttpStatus.OK.value())
                .message("shedule registered successfully")
                .timeStamp(LocalDateTime.now())
                .data(loanScheduleItem)
                .status(true)
                .build();
    }
}
