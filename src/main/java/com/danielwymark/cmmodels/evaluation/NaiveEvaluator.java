package com.danielwymark.cmmodels.evaluation;

import com.danielwymark.cmmodels.exceptions.OutOfDomainError;
import com.danielwymark.cmmodels.model.Model;
import com.danielwymark.cmmodels.model.World;
import com.danielwymark.cmmodels.syntax.CompoundFormula;
import com.danielwymark.cmmodels.syntax.Formula;
import com.danielwymark.cmmodels.syntax.AtomicFormula;

public class NaiveEvaluator implements Evaluator {
    @Override
    public boolean evaluate(Model model, World world, Formula formula) throws OutOfDomainError {
        switch (formula.operator) {
            case NONE -> {
                return model.propositionsTrueAt(world).contains((AtomicFormula) formula);
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
