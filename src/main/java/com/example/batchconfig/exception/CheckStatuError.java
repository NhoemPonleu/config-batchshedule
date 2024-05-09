package com.example.batchconfig.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
public class CheckStatuError extends ApiExceptionResponseMessage {
    private String resourceId;

    public CheckStatuError(String message, String resourceId) {
        super(HttpStatus.NOT_FOUND, message);
        this.resourceId = resourceId;
    }
}
