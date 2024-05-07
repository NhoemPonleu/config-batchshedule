package com.example.batchconfig.loan;

import com.example.batchconfig.loan.transaction.GenerateScheduleDTO;

import java.math.BigDecimal;
import java.util.List;

public interface LoanService {
    LoanReposeDTO registerNewLoan(LoanRequestDTO loanRequestDTO);

    GenerateScheduleDTO generateLoanSchedule(RequestSheduleDTO requestSheduleDTO);
    void loanRepayment(String loanAccountNumber, BigDecimal repaymentAmount);
    List<Loan> listOfLoan();
    void accruedInterestEveryday();
}
