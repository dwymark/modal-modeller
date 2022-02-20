package com.danielwymark.cmmodels.core.syntax;

import java.util.Objects;

/**
 * Represents a single proposition.
 */
public final class AtomicFormula extends Formula {
    public final String letter;

    public final static AtomicFormula TOP = new AtomicFormula("⏉");
    public final static AtomicFormula BOTTOM = new AtomicFormula("⏊");

    public AtomicFormula(String letter) {
        super(Operator.NONE);
        this.letter = letter;
    }

    public boolean alwaysTrue() {
        return letter.equals("⏉");
    }

    public boolean alwaysFalse() {
        return letter.equals("⏊");
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
