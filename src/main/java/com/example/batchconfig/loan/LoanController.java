package com.example.batchconfig.loan;

import com.example.batchconfig.baseResponse.BaseApi;
import com.example.batchconfig.exception.ResourceNotFoundException1;
import com.example.batchconfig.loan.transaction.GenerateScheduleDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/loan")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping
    public BaseApi<?>registerNewLoan(@RequestBody LoanRequestDTO loanRequestDTO) throws AccountNotFoundException {
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
    @PostMapping("/{loanAccountNumber}/repay")
    public ResponseEntity<String> repayLoan(@PathVariable String loanAccountNumber, @RequestParam BigDecimal repaymentAmount) {
        try {
            loanService.loanRepayment(loanAccountNumber, repaymentAmount);
            return ResponseEntity.ok("Loan repayment successful.");
        } catch (ResourceNotFoundException1 e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred: " + e.getMessage());
        }
    }
}
