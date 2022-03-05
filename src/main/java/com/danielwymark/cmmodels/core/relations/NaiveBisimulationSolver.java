package com.danielwymark.cmmodels.core.relations;

import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: Document this thoroughly
public class NaiveBisimulationSolver implements BisimulationSolver {
    private final Logger logger = LogManager.getLogger();

    public List<Block> findCoarsestPartitioning(Model model1, Model model2) {
        Set<World> universe = Stream.concat(model1.getWorlds().stream(), model2.getWorlds().stream())
                .collect(Collectors.toSet());
        List<PartitionFilter> filters = new ArrayList<>();

        filters.add(w -> {
            Set<World> s = Stream.concat(
                            model1.worldsAccessibleFrom(w).stream(),
                            model2.worldsAccessibleFrom(w).stream())
                    .collect(Collectors.toSet());
            logger.trace("    " + w + " -> " + s);
            return s;
        });

        // Find the coarsest stable partitioning using the parameters above
        var partitioner = new Partitioner(universe, filters);
        partitioner.refine();
        return partitioner.getBlocks();
    }

    @Override
    public Relation findLargestBisimulation(Model model1, Model model2) {
        logger.trace("Searching for bisimulation between " + model1 + " and " + model2);

        List<World> allWorlds = new ArrayList<>(model1.getWorlds());
        allWorlds.addAll(model2.getWorlds());

        List<Block> partitioning = findCoarsestPartitioning(model1, model2);

        // Construct relation from resulting partitioning
        Map<World, Set<World>> mapping = new HashMap<>();
        for (var world : allWorlds) {
            mapping.put(world, new HashSet<>());
        }

        for (var block : partitioning) {
            Set<World> worlds = block.worlds();
            for (var world1 : worlds) {
                for (var world2 : worlds) {
                    if (world1.modelId() != world2.modelId()) {
                        mapping.get(world1).add(world2);
                        mapping.get(world2).add(world1);
                    }
                }
            }
        }
        return new Relation(model1, model2, mapping);
    }

}
