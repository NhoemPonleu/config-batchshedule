package com.example.batchconfig.sendBalance.constand;

import lombok.Getter;

@Getter
public enum SendTypeCode {
    TO_ACCOUNT("A", "To Account"),
    NORMAL("N", "Normal");

    private final String code;
    private final String description;

    SendTypeCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
