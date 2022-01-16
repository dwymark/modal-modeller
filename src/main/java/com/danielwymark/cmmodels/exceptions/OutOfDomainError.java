package com.danielwymark.cmmodels.exceptions;

public class OutOfDomainError extends RuntimeException {
    public OutOfDomainError(String errorMessage) {
        super(errorMessage);
    }
}
