package com.example.batchconfig.sendBalance.constand;

import lombok.Getter;

@Getter
public enum FeeTypeCode {
    SENDER("S", "Sender Fee"),
    RECEIVER("T", "Receiver Fee");

    private final String code;
    private final String description;

    FeeTypeCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
