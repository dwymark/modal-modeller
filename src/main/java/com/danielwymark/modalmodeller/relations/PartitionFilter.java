package com.danielwymark.modalmodeller.relations;

import com.danielwymark.modalmodeller.model.World;

import java.util.Set;

public interface PartitionFilter {
    Set<World> filter(World world);
}
