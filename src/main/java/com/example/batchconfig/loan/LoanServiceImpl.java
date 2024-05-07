package com.example.batchconfig.loan;

import com.example.batchconfig.exception.ResourceNotFoundException;
import com.example.batchconfig.exception.ResourceNotFoundException1;
import com.example.batchconfig.loan.transaction.GenerateScheduleDTO;
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
        Double percentage=loanRequestDTO.getInterestRate()* loanRequestDTO.getLoanTerm()/100;
        String accountNumber = generateAccountNumber(loanRequestDTO.getBrandId());
        loan.setLoanAmount(loanRequestDTO.getLoanAmount());
        loan.setLoanTerm(loanRequestDTO.getLoanTerm());
        loan.setInterestRate(loanRequestDTO.getInterestRate());
        loan.setLoanAccountNumber(accountNumber);
        loan.setLoanPercentage(percentage);
        loanRepository.save(loan);

//        // register into table transaction
//        LoanTransactions loanTransactions=new LoanTransactions();
//        loanTransactions.setFirstBalance(loan.getLoanAmount());
//        loanTransactions.setLoan(loan.getLoanAccountNumber());
//        transactionRepository.save(loanTransactions);

        // response to client
        LoanReposeDTO loanReposeDTO = new LoanReposeDTO();
        loanReposeDTO.setLoanAmount(loanRequestDTO.getLoanAmount());
        loanReposeDTO.setLoanTerm(loanRequestDTO.getLoanTerm());
        loanReposeDTO.setLoanAccountNumber(loan.getLoanAccountNumber());
        return loanReposeDTO;
    }
@Override
    public GenerateScheduleDTO generateLoanSchedule(RequestSheduleDTO requestSheduleDTO) {
        List<LoanScheduleItem> loanScheduleItems = new ArrayList<>();
        Loan loan = loanRepository.findById(requestSheduleDTO.getLoanId())
                .orElseThrow(() -> new ResourceNotFoundException("Loan", requestSheduleDTO.getLoanId()));

        BigDecimal remainingBalance = loan.getLoanAmount();
        BigDecimal annualInterestRate = BigDecimal.valueOf(loan.getLoanPercentage());
        BigDecimal monthlyInterestRate = annualInterestRate.divide(BigDecimal.valueOf(12), 8, BigDecimal.ROUND_HALF_UP);
        BigDecimal monthlyPayment = calculateMonthlyPayment(remainingBalance, monthlyInterestRate, loan.getLoanTerm());

        BigDecimal totalInterest = BigDecimal.ZERO; // Initialize total interest
        BigDecimal totalPrincipal = BigDecimal.ZERO; // Initialize total principal

        for (int period = 1; period <= loan.getLoanTerm(); period++) {
            BigDecimal interest = remainingBalance.multiply(monthlyInterestRate);
            BigDecimal principal = monthlyPayment.subtract(interest);
            remainingBalance = remainingBalance.subtract(principal);

            // Round up payment if greater than 5
            BigDecimal payment = monthlyPayment;
            if (payment.compareTo(BigDecimal.valueOf(5)) > 0) {
                payment = payment.setScale(0, RoundingMode.UP);
            }

            LocalDate payDate = LocalDate.now().plusMonths(period); // Adjust this based on your logic

            LoanScheduleItem scheduleItem = new LoanScheduleItem(period, payDate, payment, interest, principal, remainingBalance);
            loanScheduleItems.add(scheduleItem);

            totalInterest = totalInterest.add(interest); // Add interest to total interest
            totalPrincipal = totalPrincipal.add(principal); // Add principal to total principal
        }

        return new GenerateScheduleDTO(loan.getCustomer().getFirstName()
                , loan.getLoanAccountNumber(), loanScheduleItems
                , totalInterest, totalPrincipal.add(totalInterest));
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal monthlyInterestRate, int loanTerm) {
        BigDecimal temp = BigDecimal.ONE.add(monthlyInterestRate).pow(loanTerm);
        return loanAmount.multiply(monthlyInterestRate).multiply(temp).divide(temp.subtract(BigDecimal.ONE), 2, BigDecimal.ROUND_HALF_UP);
    }
public String generateAccountNumber(String brandCode) {
    String accountNumber;
    do {
        // Increment and get the next sequence number
        long sequenceNumber = accountNumberSequence.getAndIncrement();

        // Combine brand code and sequence number
        accountNumber = brandCode + String.format("%05d", sequenceNumber);
    } while (loanRepository.existsByLoanAccountNumber(accountNumber)); // Check for uniqueness in the database

    return accountNumber;
}


    private final ConcurrentHashMap<String, Boolean> accountNumbersMap = new ConcurrentHashMap<>();
    private final AtomicLong accountNumberSequence = new AtomicLong(1L);

    @Override
    public void loanRepayment(String loanAccountNumber, BigDecimal repaymentAmount) {
        Loan loan = loanRepository.findByLoanAccountNumber(loanAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException1("Loan", loanAccountNumber));

        // Calculate interest
        BigDecimal interest = loan.getLoanAmount().multiply(BigDecimal.valueOf(loan.getInterestRate() / 100));

        // Calculate principal
        BigDecimal principal = repaymentAmount.subtract(interest);

        // Update remaining balance
        BigDecimal remainingBalance = loan.getLoanAmount().subtract(principal);

        // Create new transaction
        LoanTransactions transaction = new LoanTransactions();
        transaction.setLoanAccountNumber(loanAccountNumber);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setPayment(repaymentAmount);
        transaction.setInterest(interest);
        transaction.setPrincipal(principal);

        // Update loan and save transaction
        loan.setLoanAmount(remainingBalance);
        loanRepository.save(loan);
        transactionRepository.save(transaction);
    }
}
