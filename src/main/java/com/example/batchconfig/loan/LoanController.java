package com.example.batchconfig.loan;

import com.example.batchconfig.baseResponse.BaseApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

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
    @PostMapping("/generate-shedule")
    public BaseApi<?>registerShedulePayment(@RequestBody LoanRequestDTO loanRequestDTO) {
        List<LoanScheduleItem> loanScheduleItem=loanService.generateLoanSchedule(loanRequestDTO);
        return BaseApi.builder()
                .code(HttpStatus.OK.value())
                .message("shedule registered successfully")
                .timeStamp(LocalDateTime.now())
                .data(loanScheduleItem)
                .status(true)
                .build();
    }
}
