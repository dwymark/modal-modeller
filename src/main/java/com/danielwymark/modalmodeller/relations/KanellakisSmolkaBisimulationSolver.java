package com.danielwymark.modalmodeller.relations;

import com.danielwymark.modalmodeller.model.Model;
import com.danielwymark.modalmodeller.model.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class KanellakisSmolkaBisimulationSolver implements BisimulationSolver {
    private static final Logger logger = LogManager.getLogger(KanellakisSmolkaBisimulationSolver.class);

    @Override
    public List<Relation> findBisimulations(Model model1, Model model2) {
        logger.debug("Finding bisimulations between " + model1 + " and " + model2);
        Set<World> allWorlds = new HashSet<>(model1.getWorlds());
        allWorlds.addAll(model2.getWorlds());
        List<Model> models = new ArrayList<>(List.of(model1, model2));
        Set<Set<World>> partitions = new HashSet<>(Set.of(allWorlds));
        logger.trace(partitions.toString());
        boolean changed = true;
        while (changed) {
            changed = false;
            for (var block : partitions) {
                Set<Set<World>> splitResult = split(models, block, partitions);
                if (splitResult.size() > 1) {
                    partitions.remove(block);
                    partitions.addAll(splitResult);
                    logger.trace(partitions.toString());
                }
            }
        }
        return null;
    }

    private Set<Set<World>> split(List<Model> models, Set<World> block, Set<Set<World>> partitions) {
        Set<World> stableWorlds = new HashSet<>();
        Set<World> splittingWorlds = new HashSet<>();
        World vantage = block.iterator().next(); // pick an arbitrary vantage point
        for (var world : block) {
            if (world == vantage) {
                stableWorlds.add(world);
                continue;
            }
            for (var partition : partitions) {
                if (connectsTo(models, vantage, partition) == connectsTo(models, world, partition)) {
                    stableWorlds.add(world);
                }
                else {
                    splittingWorlds.add(world);
                }
            }
        }

        Set<Set<World>> result = new HashSet<>(Set.of(stableWorlds));
        if (!splittingWorlds.isEmpty()) {
            result.add(splittingWorlds);
        }

        logger.trace("-- Split --\nBlock: " + block + "\nPartitions: " + partitions + "\nSplit: " + result);
        return result;
    }

    private boolean connectsTo(List<Model> models, World base, Set<World> block) {
        Set<World> neighbors = new HashSet<>();
        for (var model : models) {
            neighbors.addAll(model.worldsAccessibleFrom(base));
        }

        for (var world : block) {
            if (neighbors.contains(world))
                return true;
        }
        return false;
    }
}
