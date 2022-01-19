package com.danielwymark.cmmodels.core.relations;

import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NaiveBisimulationSolver implements BisimulationSolver {
    private final Logger logger = LogManager.getLogger(NaiveBisimulationSolver.class);

    @Override
    public Relation findLargestBisimulation(Model model1, Model model2) {
        System.out.println("Searching for bisimulation between " + model1 + " and " + model2);
        Set<World> universe = Stream.concat(model1.getWorlds().stream(), model2.getWorlds().stream())
                .collect(Collectors.toSet());
        List<PartitionFilter> filters = new ArrayList<>();

        filters.add(w -> {
            var s = Stream.concat(
                            model1.worldsAccessibleFrom(w).stream(),
                            model2.worldsAccessibleFrom(w).stream())
                    .collect(Collectors.toSet());
            logger.trace("    " + w + " -> " + s);
            return s;
        });

        // Find the coarsest stable partitioning using the parameters above
        var partitioner = new Partitioner(universe, filters);
        partitioner.refine();
        List<Block> partitioning = partitioner.getBlocks();

        // Construct relation from resulting partitioning
        Map<World, World> mapping = new HashMap<>();
        for (var block : partitioning) {
            Set<World> worlds = block.worlds();
            for (var world1 : worlds) {
                for (var world2 : worlds) {
                    if (world1.modelId() != world2.modelId()) {
                        mapping.put(world1, world2);
                    }
                }
            }
        }
        return new Relation(model1, model2, mapping);
    }

}
