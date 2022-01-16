package com.danielwymark.cmmodels.syntax;

public abstract class Formula {
    public final Operator operator;

    public Formula(Operator operator) {
        this.operator = operator;
    }

    public static Formula negate(Formula formula) {
        return new CompoundFormula(Operator.NEGATE, formula);
    }

    public static Formula must(Formula formula) {
        return new CompoundFormula(Operator.MUST, formula);
    }

    public static Formula might(Formula formula) {
        return new CompoundFormula(Operator.MIGHT, formula);
    }

    public Formula and(Formula conjunct) {
        return new CompoundFormula(Operator.MEET, this, conjunct);
    }

    public Formula or(Formula disjunct) {
        return new CompoundFormula(Operator.JOIN, this, disjunct);
    }

    public Formula implies(Formula consequent) {
        return Formula.negate(this).or(consequent);
    }
}
