package com.example.batchconfig.loan.transaction;

public enum TransactionTypeCode {
    NEW_LOAN("01", "register new loans"),
    REPAY_LOAN("02", "register pay loans");
    private final String code;
    private final String description;

    TransactionTypeCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

