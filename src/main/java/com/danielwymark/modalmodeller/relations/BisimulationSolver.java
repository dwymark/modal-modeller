package com.danielwymark.modalmodeller.relations;

import com.danielwymark.modalmodeller.model.Model;

import java.util.List;

public interface BisimulationSolver {
    List<Relation> findBisimulations(Model model1, Model model2);
}
