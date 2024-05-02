package com.example.batchconfig.account.transaction;

public enum TransactionType {
    FIRST_REGISTER_ACCOUNT("01", "register new account"),
    BATCH_ACCRUED_INTEREST("02", "Batch accrued interest");

    private final String code;
    private final String description;

    TransactionType(String code, String description) {
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
