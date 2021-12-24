package com.danielwymark.modalmodeller.exceptions;

public class OutOfDomainError extends RuntimeException {
    public OutOfDomainError(String errorMessage) {
        super(errorMessage);
    }
}
