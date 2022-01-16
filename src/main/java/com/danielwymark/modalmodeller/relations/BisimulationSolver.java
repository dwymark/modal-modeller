package com.danielwymark.modalmodeller.relations;

import com.danielwymark.modalmodeller.model.Model;

import java.util.List;

public interface BisimulationSolver {
    Relation findLargestBisimulation(Model model1, Model model2);
}
