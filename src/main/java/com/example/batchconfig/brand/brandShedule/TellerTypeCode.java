package com.example.batchconfig.brand.brandShedule;

public enum TellerTypeCode {
    BATCH_TELLER("9999","teller_batch");

    private final String code;
    private final String description;

    TellerTypeCode(String code, String description) {
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
