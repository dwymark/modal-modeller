package com.danielwymark.cmmodels.exceptions;

public class GeneratorExhaustedError extends RuntimeException {
    public GeneratorExhaustedError(String errorMessage) {
        super(errorMessage);
    }
}
