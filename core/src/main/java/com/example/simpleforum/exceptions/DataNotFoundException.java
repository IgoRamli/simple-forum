package com.example.simpleforum.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class DataNotFoundException extends EntityNotFoundException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
