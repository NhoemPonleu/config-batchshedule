package com.example.batchconfig.loan;

import com.example.batchconfig.loan.transaction.GenerateScheduleDTO;

public interface LoanService {
    LoanReposeDTO registerNewLoan(LoanRequestDTO loanRequestDTO);

    GenerateScheduleDTO generateLoanSchedule(RequestSheduleDTO requestSheduleDTO);
}
