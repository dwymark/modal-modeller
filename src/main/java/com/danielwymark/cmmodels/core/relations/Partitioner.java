package com.danielwymark.cmmodels.core.relations;

import com.danielwymark.cmmodels.core.model.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Partitioner {
    private final Logger logger = LogManager.getLogger(Partitioner.class);

    private final Set<World> universe;
    private List<Block> blocks;
    private final List<PartitionFilter> filters;

    public Partitioner(Set<World> universe, List<PartitionFilter> filters) {
        this.universe = universe;
        this.filters = filters;
        this.blocks = new ArrayList<>(Collections.singleton(new Block(universe, new ArrayList<>())));
    }

    public void refine() {
        logger.trace("Beginning refinement. Step 0 = " + blocks);
        int step = 1;
        while (refineOneStep()) {
            logger.trace("Step " + step++ + " = " + blocks);
        }
        logger.trace("Completed refinement.");
    }

    // false --> nothing left to refine
    // true --> refined some block
    private boolean refineOneStep() {
        Integer refiningBlockIdx = firstUnrefinedBlockIdx();
        if (refiningBlockIdx == null) {
            return false;
        }
        Block refiningBlock = blocks.get(refiningBlockIdx);
        List<Block> newBlocks = new ArrayList<>(blocks);

        for (var filter : filters) {
            if (refiningBlock.refinedBy().contains(filter))
                continue;

            Map<Set<Integer>, Block> refinedBlocks = new HashMap<>();
            for (var world : refiningBlock.worlds()) {
                Set<World> filteredWorlds = filter.filter(world);
                Set<Integer> filteredBlocks = new HashSet<>();

                // Map each filtered world to the idx of the block containing it
                for (var filteredWorld : filteredWorlds) {
                    for (int i = 0; i < newBlocks.size(); ++i) {
                        if (newBlocks.get(i).worlds().contains(filteredWorld)) {
                            filteredBlocks.add(i);
                            break;
                        }
                    }
                }

                // Use the indices of blocks constructed above to partition worlds into blocks
                if (refinedBlocks.containsKey(filteredBlocks)) {
                    refinedBlocks.get(filteredBlocks).worlds().add(world);
                }
                else {
                    var refinedWorlds = new HashSet<>(Collections.singleton(world));
                    var refinedFilterList = new ArrayList<>(refiningBlock.refinedBy());
                    refinedBlocks.put(filteredBlocks, new Block(refinedWorlds, refinedFilterList));
                }
            }

            if (refinedBlocks.size() == 1) {
                assert (newBlocks.get(refiningBlockIdx) == refinedBlocks.values().stream().findFirst().get());
                newBlocks.get(refiningBlockIdx).refinedBy().add(filter);
            }
            else {
                newBlocks.remove(refiningBlockIdx.intValue());
                newBlocks.addAll(refinedBlocks.values());
            }
            blocks = newBlocks;
            return true;
        }
        return false;
    }

    private Integer firstUnrefinedBlockIdx() {
        for (int i = 0; i < blocks.size(); ++i) {
           var block = blocks.get(i);
            if (block.refinedBy().size() < filters.size())
               return i;
        }
        return null;
    }




    //------------------------------------------------------------------------------------------------------------------
    // Accessors
    //------------------------------------------------------------------------------------------------------------------

    public Set<World> getUniverse() {
        return Collections.unmodifiableSet(universe);
    }

    public List<Block> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    public List<PartitionFilter> getFilters() {
        return Collections.unmodifiableList(filters);
    }
}
