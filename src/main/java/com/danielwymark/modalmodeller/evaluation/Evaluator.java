package com.danielwymark.modalmodeller.evaluation;

import com.danielwymark.modalmodeller.exceptions.OutOfDomainError;
import com.danielwymark.modalmodeller.model.Model;
import com.danielwymark.modalmodeller.model.World;
import com.danielwymark.modalmodeller.syntax.Formula;

public interface Evaluator {
    /**
     * Evaluate whether a particular pointed modal model satisfies a formula.
     * `model` is assumed to be constructed over the same set of propositions as `formula`.
     *
     * @return true if `model, world |= formula`.
     * @throws OutOfDomainError if `world` is not in `model`.
     */
    boolean evaluate(Model model, World world, Formula formula) throws OutOfDomainError;
}
