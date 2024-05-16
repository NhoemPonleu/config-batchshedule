package com.example.batchconfig.loan;

import com.example.batchconfig.loan.transaction.GenerateScheduleDTO;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;

public interface LoanService {
    LoanReposeDTO registerNewLoan(LoanRequestDTO loanRequestDTO) throws AccountNotFoundException;

    GenerateScheduleDTO generateLoanSchedule(RequestSheduleDTO requestSheduleDTO);
    void loanRepayment(String loanAccountNumber, BigDecimal repaymentAmount);
    List<Loan> listOfLoan();
    void accruedInterestEveryday();
   // Loan findLoan(String loanAccountNumber);
   void settleLoansForDepositAccounts();
}
