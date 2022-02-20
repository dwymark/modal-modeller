package com.danielwymark.cmmodels.core.constraints;

import com.danielwymark.cmmodels.core.evaluation.Evaluator;
import com.danielwymark.cmmodels.core.evaluation.NaiveEvaluator;
import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.syntax.AtomicFormula;
import com.danielwymark.cmmodels.core.syntax.CompoundFormula;
import com.danielwymark.cmmodels.core.syntax.Formula;

import java.util.concurrent.atomic.AtomicBoolean;

public class AcyclicConstraint implements Constraint {
    private static final Evaluator evaluator = new NaiveEvaluator();

    @Override
    public boolean holdsOf(Model model) {
        // maxAcyclicLength: you cannot take more than |W| steps without being in a cycle
        int maxAcyclicLength = model.getNumWorlds();

        // assume acyclic until proven otherwise
        AtomicBoolean acyclic = new AtomicBoolean(true);
        model.getWorlds().parallelStream().forEach(world -> {
            if (!acyclic.get())
                return;
            Formula cursedFormula = AtomicFormula.BOTTOM;
            for (int i = 0; i < maxAcyclicLength; ++i) {
                cursedFormula = Formula.must(cursedFormula);
                if (evaluator.evaluate(model, world, cursedFormula))
                    return;
            }
            // if the cursed formula was false for i=(0, ..., |W|), then M must be cyclic by the pigeonhole principle.
            acyclic.set(false);
        });
        return acyclic.get();
    }
}
