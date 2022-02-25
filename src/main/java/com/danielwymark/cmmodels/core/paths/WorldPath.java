package com.danielwymark.cmmodels.core.paths;

import com.danielwymark.cmmodels.core.model.World;

import java.util.*;

@SuppressWarnings("unused")
public class WorldPath {
    private final List<World> worldsList;

    public WorldPath(World... worlds) {
        worldsList = Collections.unmodifiableList(Arrays.stream(worlds).toList());
    }

    public WorldPath(List<World> worlds) {
        worldsList = Collections.unmodifiableList(worlds);
    }

    public WorldPath(World world) {
        worldsList = List.of(world);
    }

    public World getFirst() {
        return worldsList.get(0);
    }

    public World getLast() {
        return worldsList.get(worldsList.size() - 1);
    }

    public int size() {
        return worldsList.size() - 1; // count transitions, not worlds
    }

    public boolean contains(World world) { return worldsList.contains(world); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorldPath worldPath = (WorldPath) o;
        return Objects.equals(worldsList, worldPath.worldsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldsList);
    }
}
