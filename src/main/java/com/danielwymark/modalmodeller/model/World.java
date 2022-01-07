package com.danielwymark.modalmodeller.model;

import java.util.Objects;

/**
 * Represents a single world in a possible worlds model.
 * Essentially, this is just a wrapper over an index.
 * The identifier is used to tie a world to its model.
 */
public record World(int index, long identifier) {

    @Override
    public int index() {
        return index;
    }

    @Override
    public String toString() {
        return "W(" + index + ")-" + identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        World world = (World) o;
        return index == world.index && identifier == world.identifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, identifier);
    }
}
