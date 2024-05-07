package com.example.batchconfig.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException1 extends RuntimeException {
    private String resourceName;
    private String resourceId;

    public ResourceNotFoundException1(String resourceName, String resourceId) {
        super(String.format("%s not found for id=%d", resourceName, resourceId));
        this.resourceName = resourceName;
        this.resourceId = String.valueOf(resourceId);
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getResourceId() {
        return resourceId;
    }
}
