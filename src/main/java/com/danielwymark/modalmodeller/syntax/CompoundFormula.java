package com.danielwymark.modalmodeller.syntax;

import java.util.Objects;

/**
 * Represents a compound of SingularFormulas in a language with one
 * pair of modal operators.
 */
public final class CompoundFormula extends Formula {
    public final Formula operand1;
    public final Formula operand2;

    public CompoundFormula(Operator operator, Formula operand1, Formula operand2) {
        super(operator);
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    public CompoundFormula(Operator operator, Formula operand) {
        super(operator);
        this.operand1 = operand;
        operand2 = null;
    }

    private String operatorString() {
        return switch (operator) {
            case NONE -> "";
            case NEGATE -> "¬";
            case JOIN -> "⋁";
            case MEET -> "⋀";
            case MUST -> "□";
            case MIGHT -> "◇";
        };
    }

    @Override
    public String toString() {
        return switch (operator) {
            case NEGATE, MUST, MIGHT -> operatorString() + operand1;
            default -> "(" + operand1 + operatorString() + operand2 + ")";
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompoundFormula that = (CompoundFormula) o;
        return operator == that.operator && Objects.equals(operand1, that.operand1) && Objects.equals(operand2, that.operand2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, operand1, operand2);
    }
}
