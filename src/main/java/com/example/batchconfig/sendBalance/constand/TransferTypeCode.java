package com.example.batchconfig.sendBalance.constand;

import lombok.Getter;

@Getter
public enum TransferTypeCode {
    NORMAL("N", "Normal"),
    CANCEL("C", "Cancel");

    private final String code;
    private final String description;

    TransferTypeCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
