package com.danielwymark.cmmodels.relations;

import com.danielwymark.cmmodels.model.World;

import java.util.Set;

public interface PartitionFilter {
    Set<World> filter(World world);
}
