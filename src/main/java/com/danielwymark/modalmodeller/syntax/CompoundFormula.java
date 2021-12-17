package com.danielwymark.modalmodeller.syntax;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents a compound of SingularFormulas in a language with one
 * pair of modal operators.
 */
public final class CompoundFormula extends Formula {
    public final Operator operator;
    public final Formula[] operands;

    public CompoundFormula(Operator operator, Formula operand) {
        this.operator = operator;
        this.operands = new Formula[]{operand};
    }

    public CompoundFormula(Operator operator, Formula operand1, Formula operand2) {
        this.operator = operator;
        this.operands = new Formula[]{operand1, operand2};
    }

    private String operatorString() {
        return switch (operator) {
            case NEGATE -> "¬";
            case JOIN -> "⋁";
            case MEET -> "⋀";
            case MUST -> "□";
            case MIGHT -> "◇";
        };
    }

    @Override
    public String toString() {
        List<String> args = Arrays.stream(operands).map(Object::toString).toList();
        return switch (operator) {
            case NEGATE, MUST, MIGHT -> operatorString() + operands[0];
            default -> "(" + String.join(operatorString(), args) + ")";
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompoundFormula that = (CompoundFormula) o;
        return operator == that.operator && Arrays.equals(operands, that.operands);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(operator);
        result = 31 * result + Arrays.hashCode(operands);
        return result;
    }
}
