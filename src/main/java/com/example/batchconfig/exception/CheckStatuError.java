package com.example.batchconfig.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CheckStatuError extends ApiExceptionResponseMessage {
    private final String resourceId;

    public CheckStatuError(String message,String resourceName, String resourceId) {
        super(HttpStatus.NOT_FOUND, message);
        this.resourceId = resourceId;
    }
}
