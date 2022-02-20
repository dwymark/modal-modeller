package com.danielwymark.cmmodels.core.evaluation;

import com.danielwymark.cmmodels.core.exceptions.OutOfDomainError;
import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.World;
import com.danielwymark.cmmodels.core.syntax.AtomicFormula;
import com.danielwymark.cmmodels.core.syntax.CompoundFormula;
import com.danielwymark.cmmodels.core.syntax.Formula;

public class NaiveEvaluator implements Evaluator {
    @Override
    public boolean evaluate(Model model, World world, Formula formula) throws OutOfDomainError {
        switch (formula.operator) {
            case NONE -> {
                var atomicFormula = ((AtomicFormula) formula);
                if (atomicFormula.alwaysTrue()) {
                    return true;
                }
                if (atomicFormula.alwaysFalse()) {
                    return false;
                }
                return model.propositionsTrueAt(world).contains(atomicFormula);
            }
            case NEGATE -> {
                return !evaluate(model, world, ((CompoundFormula) formula).operand1);
            }
            case JOIN -> {
                return evaluate(model, world, ((CompoundFormula) formula).operand1)
                        || evaluate(model, world, ((CompoundFormula) formula).operand2);
            }
            case MEET -> {
                return evaluate(model, world, ((CompoundFormula) formula).operand1)
                        && evaluate(model, world, ((CompoundFormula) formula).operand2);
            }
            case MUST -> {
                for (var neighbor : model.worldsAccessibleFrom(world)) {
                    if (!evaluate(model, neighbor, ((CompoundFormula) formula).operand1))
                        return false;
                }
                return true;
            }
            case MIGHT -> {
                for (var neighbor : model.worldsAccessibleFrom(world)) {
                    if (evaluate(model, neighbor, ((CompoundFormula) formula).operand1))
                        return true;
                }
                return false;
            }
        }
        throw new IllegalArgumentException();
    }
}
