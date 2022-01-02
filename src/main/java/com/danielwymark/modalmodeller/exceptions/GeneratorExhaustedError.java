package com.danielwymark.modalmodeller.exceptions;

public class GeneratorExhaustedError extends RuntimeException {
    public GeneratorExhaustedError(String errorMessage) {
        super(errorMessage);
    }
}
