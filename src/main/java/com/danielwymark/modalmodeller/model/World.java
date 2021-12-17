package com.danielwymark.modalmodeller.model;

import java.util.Objects;

public class World {
    public final int index;

    public World(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "W(" + index + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        World world = (World) o;
        return index == world.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
