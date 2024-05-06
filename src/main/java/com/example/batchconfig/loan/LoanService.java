package com.example.batchconfig.loan;

import java.util.List;

public interface LoanService {
    LoanReposeDTO registerNewLoan(LoanRequestDTO loanRequestDTO);
    List<LoanScheduleItem> generateLoanSchedule(LoanRequestDTO loanRequestDTO);
}
