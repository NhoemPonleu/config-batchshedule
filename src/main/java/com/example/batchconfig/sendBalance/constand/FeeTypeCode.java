package com.example.batchconfig.sendBalance.constand;

public enum FeeTypeCode {
    SENDER("S", "Sender Fee"),
    RECEIVER("T", "Receiver Fee");

    private final String code;
    private final String description;

    FeeTypeCode(String code, String description) {
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
