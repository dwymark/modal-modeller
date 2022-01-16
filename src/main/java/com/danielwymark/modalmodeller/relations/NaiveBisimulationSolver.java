package com.danielwymark.modalmodeller.relations;

import com.danielwymark.modalmodeller.model.Model;
import com.danielwymark.modalmodeller.model.World;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NaiveBisimulationSolver implements BisimulationSolver {
    private final Logger logger = LogManager.getLogger(NaiveBisimulationSolver.class);

    @Override
    public List<Relation> findBisimulations(Model model1, Model model2) {
        Set<World> universe = Stream.concat(model1.getWorlds().stream(), model2.getWorlds().stream())
                .collect(Collectors.toSet());
        List<PartitionFilter> filters = new ArrayList<>();
        filters.add(w -> Stream.concat(
                        model1.worldsAccessibleFrom(w).stream(),
                        model2.worldsAccessibleFrom(w).stream())
                .collect(Collectors.toSet()));
        var partitioner = new Partitioner(universe, filters);

        logger.debug(partitioner.getBlocks());
        while (partitioner.refine()) {
            logger.debug(partitioner.getBlocks());
        }

        //TODO
        return null;
    }

}
