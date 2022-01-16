package com.danielwymark.cmmodels.core.exceptions;

public class OutOfDomainError extends RuntimeException {
    public OutOfDomainError(String errorMessage) {
        super(errorMessage);
    }
}
