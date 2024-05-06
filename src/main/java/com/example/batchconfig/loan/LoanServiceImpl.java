package com.example.batchconfig.loan;

import com.example.batchconfig.loan.transaction.LoanTransactions;
import com.example.batchconfig.loan.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final TransactionRepository transactionRepository;
    @Override
    public LoanReposeDTO registerNewLoan(LoanRequestDTO loanRequestDTO) {
        Loan loan = new Loan();
        String accountNumber = generateAccountNumber(loanRequestDTO.getBrandId());
        loan.setLoanAmount(loanRequestDTO.getLoanAmount());
        loan.setLoanTerm(loanRequestDTO.getLoanTerm());
        loan.setInterestRate(loanRequestDTO.getInterestRate());
        loan.setLoanAccountNumber(accountNumber);
        loanRepository.save(loan);

        // register into table transaction
        LoanTransactions loanTransactions=new LoanTransactions();
        loanTransactions.setFirstBalance(loan.getLoanAmount());
        loanTransactions.setLoan(loan.getLoanAccountNumber());
        transactionRepository.save(loanTransactions);

        // response to client
        LoanReposeDTO loanReposeDTO = new LoanReposeDTO();
        loanReposeDTO.setLoanAmount(loanRequestDTO.getLoanAmount());
        loanReposeDTO.setLoanTerm(loanRequestDTO.getLoanTerm());
        loanReposeDTO.setLoanAccountNumber(loan.getLoanAccountNumber());
        return loanReposeDTO;
    }

    @Override
    public List<LoanScheduleItem> generateLoanSchedule(LoanRequestDTO loanRequestDTO) {
        List<LoanScheduleItem> schedule = new ArrayList<>();

        BigDecimal loanAmount = loanRequestDTO.getLoanAmount();
        Double interestRate = loanRequestDTO.getInterestRate();
        Integer loanTerm = loanRequestDTO.getLoanTerm();

        BigDecimal monthlyInterestRate = BigDecimal.valueOf(interestRate / 100 / 12);
        BigDecimal monthlyPayment = calculateMonthlyPayment(loanAmount, monthlyInterestRate, loanTerm);

        BigDecimal remainingBalance = loanAmount;
        for (int period = 1; period <= loanTerm; period++) {
            BigDecimal interest = remainingBalance.multiply(monthlyInterestRate);
            BigDecimal principal = monthlyPayment.subtract(interest);
            remainingBalance = remainingBalance.subtract(principal);

            LocalDate payDate = LocalDate.now().plusMonths(period); // Adjust as needed

            LoanScheduleItem scheduleItem = new LoanScheduleItem(period, payDate, monthlyPayment, interest, principal, remainingBalance);
            schedule.add(scheduleItem);
        }

        return schedule;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal monthlyInterestRate, Integer loanTerm) {
        BigDecimal factor = monthlyInterestRate.add(BigDecimal.ONE).pow(loanTerm);
        BigDecimal numerator = loanAmount.multiply(monthlyInterestRate).multiply(factor);
        BigDecimal denominator = factor.subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
    }
    public String generateAccountNumber(String brandCode) {
        String accountNumber;
        do {
            // Increment and get the next sequence number
            long sequenceNumber = accountNumberSequence.getAndIncrement();

            // Combine brand code and sequence number
            accountNumber = brandCode  + String.format("%05d", sequenceNumber);
        } while (accountNumbersMap.putIfAbsent(accountNumber, true) != null); // Check for uniqueness

        return accountNumber;
    }
    private final ConcurrentHashMap<String, Boolean> accountNumbersMap = new ConcurrentHashMap<>();
    private final AtomicLong accountNumberSequence = new AtomicLong(1L);
}
