package com.example.batchconfig.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientBalanceException extends ApiExceptionResponseMessage {

    public InsufficientBalanceException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
