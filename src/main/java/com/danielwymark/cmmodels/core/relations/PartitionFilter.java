package com.danielwymark.cmmodels.core.relations;

import com.danielwymark.cmmodels.core.model.World;

import java.util.Set;

public interface PartitionFilter {
    Set<World> filter(World world);
}
