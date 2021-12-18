package com.danielwymark.modalmodeller.model;

import com.danielwymark.modalmodeller.exceptions.OutOfDomainError;
import com.danielwymark.modalmodeller.syntax.SingularFormula;

import java.util.*;

/**
 * Represents a possible worlds model, which is defined by a set of worlds W,
 * an accessibility relation on worlds R, and a valuation map V(s, p) which
 * maps propositions to true or false in a world.
 */
public final class Model {
    private final List<World> worlds;
    private final Map<World, HashSet<World>> accessMap;
    private final Map<World, HashSet<SingularFormula>> valuationMap;
    private final HashMap<Integer, World> worldMap;

    public Model(List<World> worlds, Map<World, HashSet<World>> accessMap,
                 Map<World, HashSet<SingularFormula>> valuationMap) {
        this.worlds = worlds;
        worldMap = new HashMap<>();
        for (var world : worlds) {
            worldMap.put(world.index, world);
        }
        this.accessMap = accessMap;
        this.valuationMap = valuationMap;
    }

    public List<World> getWorlds() {
        return Collections.unmodifiableList(worlds);
    }

    public World getWorld(int i) throws OutOfDomainError {
        assertInDomain(i);
        return worldMap.get(i);
    }

    public Set<World> worldsAccessibleFrom(World world) throws OutOfDomainError {
        assertInDomain(world.index);
        return Collections.unmodifiableSet(accessMap.get(world));
    }

    public Set<SingularFormula> propositionsTrueAt(World world) throws OutOfDomainError {
        assertInDomain(world.index);
        return Collections.unmodifiableSet(valuationMap.get(world));
    }

    public List<World> worlds() {
        return worlds;
    }

    public Map<World, HashSet<World>> accessMap() {
        return accessMap;
    }

    public Map<World, HashSet<SingularFormula>> valuationMap() {
        return valuationMap;
    }

    private void assertInDomain(int i) throws OutOfDomainError {
        if (!worldMap.containsKey(i))
            throw new OutOfDomainError("World " + i + " not in domain of model");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Model) obj;
        return Objects.equals(this.worlds, that.worlds) &&
                Objects.equals(this.accessMap, that.accessMap) &&
                Objects.equals(this.valuationMap, that.valuationMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worlds, accessMap, valuationMap);
    }

    @Override
    public String toString() {
        return "Model[" +
                "worlds=" + worlds + ", " +
                "accessMap=" + accessMap + ", " +
                "valuationMap=" + valuationMap + ']';
    }


    // TODO: implement a graphvis output function (in a separate class)
}
