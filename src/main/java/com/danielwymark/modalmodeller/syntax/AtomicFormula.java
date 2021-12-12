package com.danielwymark.modalmodeller.syntax;

import java.util.Objects;

public class AtomicFormula extends Formula {
    public final String letter;

    public AtomicFormula(String letter) {
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
