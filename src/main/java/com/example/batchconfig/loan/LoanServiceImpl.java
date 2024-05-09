package com.example.batchconfig.loan;

import com.example.batchconfig.account.Account;
import com.example.batchconfig.account.AccountRepository;
import com.example.batchconfig.account.transaction.AccountTransaction;
import com.example.batchconfig.account.transaction.AccountTransactionRepository;
import com.example.batchconfig.customer.Customer;
import com.example.batchconfig.customer.CustomerRepository;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final AccountTransactionRepository accountTransactionRepository;
    private final AccountDepositRepository accountDepositRepository;
    private final UserAuthenticationUtils userAuthenticationUtils;

    @Override
    public LoanReposeDTO  registerNewLoan(LoanRequestDTO loanRequestDTO) {
        Optional<Customer> customerOptional = customerRepository.findById(loanRequestDTO.getCustomerId());
         List<String> accounts=accountRepository.findAccountNumbersByCustomerId(loanRequestDTO.getCustomerId());
         String accountNumber= accounts.get(0);
        Account account = accountRepository.findById(Long.valueOf(accountNumber)).orElse(null);

        if (customerOptional.isEmpty()) {
            // Handle the case where customer is not found
            // For example, throw an exception or return an error response
        }

        Customer customer = customerOptional.get();

        // Create a new loan
        Loan loan = new Loan();
        Double percentage = loanRequestDTO.getInterestRate() * loanRequestDTO.getLoanTerm() / 100;
     //   String accountNumber = generateAccountNumber(loanRequestDTO.getBrandId());
        loan.setLoanAmount(loanRequestDTO.getLoanAmount());
        loan.setLoanTerm(loanRequestDTO.getLoanTerm());
        loan.setInterestRate(loanRequestDTO.getInterestRate());
        loan.setLoanAccountNumber(accountNumber);
        loan.setLoanPercentage(percentage);
        loan.setLoanDate(LocalDate.now());
        loan.setCreditOfficerName(loanRequestDTO.getCreditOfficerName());
        loan.setLoanScheduleType(LoanSheduleTypeCodeq.valueOf(loanRequestDTO.getLaonType()));
        loan.setRegisterTellerId(userAuthenticationUtils.getUserRequestDTO().getUserId());
        loan.setRegisterTellerName(userAuthenticationUtils.getUserRequestDTO().getUsername());
        loan.setCustomer(customer);
        loan.setIdentityNo(customer.getIdentity());
        loanRepository.save(loan);
     //   Account account = loan.getAccount();

        // Create a new account deposit linked to the loan
        AccountDeposit accountDeposit = new AccountDeposit();
        accountDeposit.setAccount(account);
        accountDeposit.setDepositAmount(loanRequestDTO.getLoanAmount()); // Assuming deposit amount is the same as loan amount
        accountDeposit.setDepositDate(LocalDate.now()); // Assuming deposit date is same as loan date
        accountDeposit.setLoan(loan);
        // You may need to set other properties of account deposit

        // Save the account deposit
        accountDepositRepository.save(accountDeposit);

        // Prepare response to client
        LoanReposeDTO  loanResponseDTO = new LoanReposeDTO ();
        loanResponseDTO.setLoanAmount(loanRequestDTO.getLoanAmount());
        loanResponseDTO.setLoanTerm(loanRequestDTO.getLoanTerm());
        loanResponseDTO.setLoanAccountNumber(loan.getLoanAccountNumber());

        return loanResponseDTO;
    }


    @Override
    public GenerateScheduleDTO generateLoanSchedule(RequestSheduleDTO requestSheduleDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<LoanScheduleItem> loanScheduleItems = new ArrayList<>();
        Loan loan = loanRepository.findByLoanAccountNumber(requestSheduleDTO.getLoanId())
                .orElseThrow(() -> new ResourceNotFoundException1("Loan","", requestSheduleDTO.getLoanId()));

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
                .orElseThrow(() -> new ResourceNotFoundException1("Loan","", loanAccountNumber));
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

    public void settleLoansForDepositAccounts() {
        System.out.println("Batch job started at: " + LocalDateTime.now());

        // Get all accounts with their loans eagerly fetched
        List<Account> accounts = accountRepository.findAllWithLoans();

        for (Account account : accounts) {
            // Check if the account has a linked loan
            Set<Loan> loans = account.getLoans();
            if (loans != null && !loans.isEmpty()) {
                // Perform loan settlement for each loan
                for (Loan loan : loans) {
                    BigDecimal loanAmount = loan.getLoanAmount();
                    BigDecimal depositBalance = account.getBalance();

                    // Calculate the settlement amount
                    BigDecimal settlementAmount = depositBalance.min(loanAmount); // Settle up to the loan amount or deposit balance, whichever is smaller

                    if (settlementAmount.compareTo(BigDecimal.ZERO) > 0) {
                        // Update account balance
                        account.setBalance(depositBalance.subtract(settlementAmount));

                        // Update loan amount
                        loan.setLoanAmount(loanAmount.subtract(settlementAmount));

                        // Save changes to entities
                        accountRepository.save(account);
                        loanRepository.save(loan);

                        // Create a transaction record
                        AccountTransaction transaction = new AccountTransaction();
                        transaction.setAccountId(account.getAccountNumber());
                        transaction.setTransactionAmount(settlementAmount.negate()); // Withdrawal
                        transaction.setTransactionDate(LocalDate.now());
                        accountTransactionRepository.save(transaction);

                        System.out.println("Settled loan amount: " + settlementAmount + " for loan account: " + loan.getLoanAccountNumber());
                    }

                    // Check if loan is fully settled
                    if (loan.getLoanAmount().compareTo(BigDecimal.ZERO) <= 0) {
                        System.out.println("Loan fully settled for account: " + loan.getLoanAccountNumber());
                        break; // Stop processing this loan
                    }
                }
            }
        }

        System.out.println("Batch job completed at: " + LocalDateTime.now());
    }

//    @Override
//    public void settleLoanOnline(String depositAccountNumber) {
//        Loan loan = loanRepository.findByDepositAccountNumberIn(depositAccountNumber);
//    }

//    @Override
//    public Loan findLoan(String loanAccountNumber) {
//        return null;
//    }

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
