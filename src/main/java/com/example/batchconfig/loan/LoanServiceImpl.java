package com.example.batchconfig.loan;

import com.example.batchconfig.exception.InvalidRepaymentException;
import com.example.batchconfig.exception.ResourceNotFoundException;
import com.example.batchconfig.exception.ResourceNotFoundException1;
import com.example.batchconfig.loan.transaction.*;
import com.example.batchconfig.security.user.User;
import com.example.batchconfig.security.user.UserRequestDTO;
import com.example.batchconfig.util.UserAuthenticationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final TransactionRepository transactionRepository;
    @Autowired
    private UserAuthenticationUtils userAuthenticationUtils;

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
        loan.setLoanDate(LocalDate.now());
        loan.setCreditOfficerName(loanRequestDTO.getCreditOfficerName());
        loan.setLoanScheduleType(LoanSheduleTypeCodeq.valueOf(loanRequestDTO.getLaonType()));
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<LoanScheduleItem> loanScheduleItems = new ArrayList<>();
        Loan loan = loanRepository.findByLoanAccountNumber(requestSheduleDTO.getLoanId())
                .orElseThrow(() -> new ResourceNotFoundException1("Loan", requestSheduleDTO.getLoanId()));

        BigDecimal remainingBalance = loan.getLoanAmount();
        BigDecimal annualInterestRate = BigDecimal.valueOf(loan.getLoanPercentage());
        BigDecimal monthlyInterestRate = annualInterestRate.divide(BigDecimal.valueOf(12), 8, BigDecimal.ROUND_HALF_UP);
        BigDecimal monthlyPayment = calculateMonthlyPayment(remainingBalance, monthlyInterestRate, loan.getLoanTerm());

        BigDecimal totalInterest = BigDecimal.ZERO; // Initialize total interest
        BigDecimal totalPrincipal = BigDecimal.ZERO; // Initialize total principal

        for (int period = 1; period <= loan.getLoanTerm(); period++) {
            BigDecimal interest = remainingBalance.multiply(monthlyInterestRate);
            BigDecimal principal;

            if (period == loan.getLoanTerm()) {
                principal = remainingBalance; // Last payment should be the remaining balance
            } else {
                principal = monthlyPayment.subtract(interest);
            }

            // Round up interest and principal if the decimal part is greater than or equal to 0.5
            interest = roundDecimal(interest);
            principal = roundDecimal(principal);

            // Round up monthly payment to the nearest whole number
            if (period != loan.getLoanTerm()) {
                monthlyPayment = roundDecimal(monthlyPayment);
            } else {
                monthlyPayment = remainingBalance.add(interest).setScale(0, RoundingMode.UP);
            }

            remainingBalance = remainingBalance.subtract(principal);

            LocalDate payDate = LocalDate.now().plusMonths(period); // Adjust this based on your logic

            LoanScheduleItem scheduleItem = new LoanScheduleItem(period, payDate, monthlyPayment, interest, principal, remainingBalance);
            loanScheduleItems.add(scheduleItem);

            totalInterest = totalInterest.add(interest); // Add interest to total interest
            totalPrincipal = totalPrincipal.add(principal); // Add principal to total principal
        }

        BigDecimal totalAmount = totalPrincipal.add(totalInterest).setScale(2, RoundingMode.HALF_UP); // Calculate total amount
        LocalDate endate = LocalDate.now().plusMonths(loan.getLoanTerm());
        BigDecimal feeLoan = BigDecimal.ZERO;
        BigDecimal feeAmount = loan.getFeeRate();
        if (feeAmount == null) {
            feeLoan = BigDecimal.ZERO;
        } else {
            feeLoan = new BigDecimal(String.valueOf(feeAmount));
        }
     //   String td=getUserNameByAuthentication();
        return new GenerateScheduleDTO(
                loan.getCustomer().getFirstName(),
                loan.getLoanAmount(),
                loan.getLoanAccountNumber(),
                loanScheduleItems,
                totalInterest,
                totalAmount,
                loan.getLoanDate(),
                endate,
                loan.getLoanTerm(),
                loan.getCreditOfficerName(),
                loan.getInterestRate(),
                loan.getLoanScheduleType().getDescription(),
                feeLoan,// Get the username of the authenticated user
                userAuthenticationUtils.getUserRequestDTO()
        );
    }

        private BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal monthlyInterestRate, int loanTerm) {
        BigDecimal temp = BigDecimal.ONE.add(monthlyInterestRate).pow(loanTerm);
        return loanAmount.multiply(monthlyInterestRate).multiply(temp).divide(temp.subtract(BigDecimal.ONE), 2, BigDecimal.ROUND_HALF_UP);
    }

//    private BigDecimal roundDecimal(BigDecimal value) {
//        return value.setScale(0, RoundingMode.HALF_UP);
//    }

    private BigDecimal roundDecimal(BigDecimal value) {
        if (value.scale() > 0 && value.remainder(BigDecimal.ONE).compareTo(new BigDecimal("0.5")) >= 0) {
            return value.setScale(0, RoundingMode.UP);
        } else {
            return value.setScale(0, RoundingMode.DOWN);
        }
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
//        if (loan.getLoanAmount().compareTo(repaymentAmount) < 0) {
//            throw new InvalidRepaymentException("Repayment amount exceeds remaining loan amount");
//        }
        Long maxSeqNo = transactionRepository.findMaxSeqNoByAccountId(loan.getLoanAccountNumber());

        // Calculate interest
        BigDecimal interest = loan.getLoanAmount().multiply(BigDecimal.valueOf(loan.getInterestRate() / 100));

        // Calculate principal
        BigDecimal principal = repaymentAmount.subtract(interest);

        // Update remaining balance
        BigDecimal remainingBalance = loan.getLoanAmount().subtract(principal);

        // Create new transaction
        LoanTransactions transaction = new LoanTransactions();
        transaction.setTransactionSeqNo(maxSeqNo != null ? maxSeqNo + 1 : 1);
        transaction.setLoanAccountNumber(loanAccountNumber);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setPayment(repaymentAmount);
        transaction.setInterest(interest);
        transaction.setPrincipal(principal);
        transaction.setPaymentType(TransactionTypeCode.REPAY_LOAN.getCode());

        // Update loan and save transaction
        loan.setLoanAmount(remainingBalance);
        loanRepository.save(loan);
        transactionRepository.save(transaction);
    }

    @Override
    public List<Loan> listOfLoan() {
    List<Loan> loanList=    loanRepository.findAll();
        return loanList;
    }

    @Override
    public void accruedInterestEveryday() {
        List<Loan> loanList = listOfLoan();

        LocalDate currentDate = LocalDate.now();

        for (Loan loan : loanList) {
            BigDecimal accruedInterest = calculateAccruedInterest(loan, currentDate);
            // Update the loan's accrued interest in the database
            loan.setAccruedInterest(accruedInterest);
            LoanTransactions loanTransactions=new LoanTransactions();
            loanTransactions.setInterest(accruedInterest);
            loanTransactions.setLoanAccountNumber(loan.getLoanAccountNumber());
            transactionRepository.save(loanTransactions);
            loanRepository.save(loan);
        }
    }
    private BigDecimal calculateAccruedInterest(Loan loan, LocalDate currentDate) {
        // Calculate the daily interest rate
        double dailyInterestRate = loan.getLoanPercentage() / 365;

        // Calculate the number of days between the loan creation date and the current date
        long days = ChronoUnit.DAYS.between(loan.getLoanDate(), currentDate);

        // Calculate accrued interest
        BigDecimal accruedInterest = loan.getLoanAmount()
                .multiply(BigDecimal.valueOf(dailyInterestRate))
                .multiply(BigDecimal.valueOf(days));

        return accruedInterest;
    }
}
