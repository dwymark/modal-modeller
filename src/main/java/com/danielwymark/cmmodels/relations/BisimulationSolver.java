package com.danielwymark.cmmodels.relations;

import com.danielwymark.cmmodels.model.Model;

public interface BisimulationSolver {
    Relation findLargestBisimulation(Model model1, Model model2);
}
