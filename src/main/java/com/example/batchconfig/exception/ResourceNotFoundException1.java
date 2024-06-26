package com.example.batchconfig.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
public class ResourceNotFoundException1 extends ApiExceptionResponseMessage {
    private String resourceName;
    private String resourceId;

    public ResourceNotFoundException1(String message, String resourceName, String resourceId) {
        super(HttpStatus.NOT_FOUND, message);
        this.resourceName = resourceName;
        this.resourceId = resourceId;
    }
}

