package com.danielwymark.cmmodels.model;

import java.util.Objects;

/**
 * Represents a single world in a possible worlds model.
 * Essentially, this is just a wrapper over an index.
 * The modelId is used to tie a world to its model.
 */
public record World(int index, long modelId) {

    @Override
    public int index() {
        return index;
    }

    @Override
    public String toString() {
        return "W(" + index + ")-" + modelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        World world = (World) o;
        return index == world.index && modelId == world.modelId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, modelId);
    }
}
