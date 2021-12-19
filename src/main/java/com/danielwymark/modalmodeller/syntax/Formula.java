package com.danielwymark.modalmodeller.syntax;

public abstract class Formula {
    public final Operator operator;

    public Formula(Operator operator) {
        this.operator = operator;
    }
}
