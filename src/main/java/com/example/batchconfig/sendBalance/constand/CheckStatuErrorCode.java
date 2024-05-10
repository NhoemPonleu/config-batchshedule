package com.example.batchconfig.sendBalance.constand;

import com.example.batchconfig.exception.ApiExceptionResponseMessage;
import org.springframework.http.HttpStatus;

public class CheckStatuErrorCode extends ApiExceptionResponseMessage {
    String resourceId;
    public CheckStatuErrorCode(String message, String resourceId) {
        super(HttpStatus.NOT_FOUND, message);
        this.resourceId=resourceId;
    }
}
