package com.danielwymark.cmmodels.core.relations;

import com.danielwymark.cmmodels.core.model.World;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public record Block(Set<World> worlds, List<PartitionFilter> refinedBy) {
    @Override
    public String toString() {
        return worlds.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return Objects.equals(worlds, block.worlds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worlds);
    }
}
