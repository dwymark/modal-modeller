package com.danielwymark.modalmodeller.relations;

import com.danielwymark.modalmodeller.model.World;

import java.util.List;
import java.util.Set;

public record Block(Set<World> worlds, List<PartitionFilter> refinedBy) {
}
