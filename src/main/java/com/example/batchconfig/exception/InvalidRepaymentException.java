package com.example.batchconfig.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRepaymentException extends RuntimeException {
    public InvalidRepaymentException(String message) {
        super(message);
    }
}
