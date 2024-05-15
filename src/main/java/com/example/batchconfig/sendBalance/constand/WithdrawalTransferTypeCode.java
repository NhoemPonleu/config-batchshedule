package com.example.batchconfig.sendBalance.constand;

import lombok.Getter;

@Getter
public enum WithdrawalTransferTypeCode {
    WITHDRAWAL("W", "Withdrawal"),
    TRANSFER("T", "Transfer");

    private final String code;
    private final String description;

    WithdrawalTransferTypeCode(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
