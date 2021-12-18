package com.danielwymark.modalmodeller.model;

import com.danielwymark.modalmodeller.syntax.SingularFormula;

import java.util.*;

/**
 * Represents a possible worlds model, which is defined by a set of worlds W,
 * an accessibility relation on worlds R, and a valuation map V(s, p) which
 * maps propositions to true or false in a world.
 */
public record Model(List<World> worlds, Map<World, HashSet<World>> accessMap,
                    Map<World, HashSet<SingularFormula>> valuationMap) {

    public List<World> getWorlds() {
        return Collections.unmodifiableList(worlds);
    }

    public World getWorld(int i) {
        return worlds.get(i);
    }

    public Set<World> worldsAccessibleFrom(World world) {
        return Collections.unmodifiableSet(accessMap.get(world));
    }

    public Set<SingularFormula> propositionsTrueAt(World world) {
        return Collections.unmodifiableSet(valuationMap.get(world));
    }

    // TODO: implement a graphvis output function (in a separate class)
}
