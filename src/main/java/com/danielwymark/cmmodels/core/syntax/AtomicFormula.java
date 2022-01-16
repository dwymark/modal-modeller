package com.danielwymark.cmmodels.core.syntax;

import java.util.Objects;

/**
 * Represents a single proposition. Typically, these are called "atomic" formulas,
 * but in the context of Java that is a confusing name.
 */
public final class AtomicFormula extends Formula {
    public final String letter;

    public AtomicFormula(String letter) {
        super(Operator.NONE);
        this.letter = letter;
    }

    @Override
    public String toString() {
        return letter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AtomicFormula that = (AtomicFormula) o;
        return Objects.equals(letter, that.letter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter);
    }
}
