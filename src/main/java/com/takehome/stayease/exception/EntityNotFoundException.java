package com.takehome.stayease.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Long id, String resourceName) {
        super(resourceName + " with ID '" + id + "' not found.");
    }
    
    public EntityNotFoundException(String email, String resourceName) {
        super(resourceName + " with email '" + email + "' not found.");
    }
    
    public EntityNotFoundException(String msg) {
        super(msg);
    }
}
