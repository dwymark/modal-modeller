package com.danielwymark.cmmodels.core.relations;

import com.danielwymark.cmmodels.core.model.Model;

public interface BisimulationSolver {
    Relation findLargestBisimulation(Model model1, Model model2);
}
