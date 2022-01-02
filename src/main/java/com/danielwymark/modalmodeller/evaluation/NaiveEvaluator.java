package com.danielwymark.modalmodeller.evaluation;

import com.danielwymark.modalmodeller.exceptions.OutOfDomainError;
import com.danielwymark.modalmodeller.model.Model;
import com.danielwymark.modalmodeller.model.World;
import com.danielwymark.modalmodeller.syntax.CompoundFormula;
import com.danielwymark.modalmodeller.syntax.Formula;
import com.danielwymark.modalmodeller.syntax.AtomicFormula;

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
