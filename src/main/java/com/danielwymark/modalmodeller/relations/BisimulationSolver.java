package com.danielwymark.modalmodeller.relations;

import com.danielwymark.modalmodeller.model.Model;

import java.util.List;

public interface BisimulationSolver {
    Relation findBisimulation(Model left, Model right); // return null if none
    List<Relation> findAllBisimulations(Model left, Model right); // return empty list if none
}
