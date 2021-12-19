package com.danielwymark.modalmodeller.syntax;

import java.util.Objects;

/**
 * Represents a single proposition. Typically, these are called "atomic" formulas,
 * but in the context of Java that is a confusing name.
 */
public final class SingularFormula extends Formula {
    public final String letter;

    public SingularFormula(String letter) {
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
        SingularFormula that = (SingularFormula) o;
        return Objects.equals(letter, that.letter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter);
    }
}
