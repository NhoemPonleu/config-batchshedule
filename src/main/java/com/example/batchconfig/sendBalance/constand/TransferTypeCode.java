package com.example.batchconfig.sendBalance.constand;

public enum TransferTypeCode {
    NORMAL("N", "Normal"),
    CANCEL("C", "Cancel");

    private final String code;
    private final String description;

    TransferTypeCode(String code, String description) {
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
