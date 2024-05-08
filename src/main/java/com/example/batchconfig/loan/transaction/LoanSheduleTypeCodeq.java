package com.example.batchconfig.loan.transaction;

public enum LoanSheduleTypeCodeq {
    AMORTIZING("Amortizing Loan", "This type of loan is paid off in regular installments (often monthly) that include both principal and interest. With each payment, the amount of interest decreases while the amount of principal increases, leading to the loan being fully paid off by the end of the term."),
    INTEREST_ONLY("Interest-Only Loan", "In this type of loan, the borrower only pays the interest on the principal amount for a certain period, usually at the beginning. After the interest-only period ends, the borrower starts making payments on the principal, often resulting in higher payments."),
    BALLOON("Balloon Loan", "A balloon loan involves making small monthly payments for a certain period, with a large final payment (the balloon payment) at the end. This type of loan is often used when the borrower expects to have a large sum of money available to pay off the loan at the end of the term."),
    FIXED_RATE("Fixed-Rate Loan", "In a fixed-rate loan, the interest rate remains constant throughout the term of the loan, resulting in fixed monthly payments. This provides stability and predictability for borrowers since their payments do not change over time."),
    VARIABLE_RATE("Variable-Rate Loan (Adjustable-Rate Loan)", "With a variable-rate loan, the interest rate may change periodically based on market conditions. This can lead to fluctuations in the monthly payment amount, making it less predictable than a fixed-rate loan."),
    SECURED("Secured Loan", "A secured loan is backed by collateral, such as a car or a house. If the borrower defaults on the loan, the lender can seize the collateral to recover their losses."),
    UNSECURED("Unsecured Loan", "An unsecured loan is not backed by collateral. Lenders rely solely on the borrower's creditworthiness to approve the loan, making it riskier for lenders and often resulting in higher interest rates for borrowers.");

    private final String description;
    private final String details;

    LoanSheduleTypeCodeq(String description, String details) {
        this.description = description;
        this.details = details;
    }

    public String getDescription() {
        return description;
    }

    public String getDetails() {
        return details;
    }
}
