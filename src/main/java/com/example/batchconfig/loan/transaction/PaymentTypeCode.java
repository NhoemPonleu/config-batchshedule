package com.example.batchconfig.loan.transaction;

public enum PaymentTypeCode {
    CASH("01", "recieved cash"),
    ABA_BANK("ABAAKHPP", "aba bank"),
    OTHER_BANK("03","other bank");
    private final String code;
    private final String description;

    PaymentTypeCode(String code, String description) {
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
