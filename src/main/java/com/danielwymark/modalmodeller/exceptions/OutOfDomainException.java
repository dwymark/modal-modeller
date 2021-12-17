package com.danielwymark.modalmodeller.exceptions;

public class OutOfDomainException extends RuntimeException {
    public OutOfDomainException(String errorMessage) {
        super(errorMessage);
    }
}
