package com.danielwymark.cmmodels.core.exceptions;

public class GeneratorExhaustedError extends RuntimeException {
    public GeneratorExhaustedError(String errorMessage) {
        super(errorMessage);
    }
}
