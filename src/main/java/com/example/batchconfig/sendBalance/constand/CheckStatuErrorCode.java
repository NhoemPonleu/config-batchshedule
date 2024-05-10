package com.example.batchconfig.sendBalance.constand;

import com.example.batchconfig.exception.ApiExceptionResponseMessage;
import org.springframework.http.HttpStatus;

public class CheckStatuErrorCode extends ApiExceptionResponseMessage {
    public CheckStatuErrorCode(String message, String resourceId) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
