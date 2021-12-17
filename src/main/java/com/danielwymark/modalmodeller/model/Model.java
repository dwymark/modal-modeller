package com.danielwymark.modalmodeller.model;

import com.danielwymark.modalmodeller.exceptions.OutOfDomainException;
import com.danielwymark.modalmodeller.syntax.SingularFormula;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a possible worlds model, which is defined by a set of worlds W,
 * an accessibility relation on worlds R, and a valuation map V(s, p) which
 * maps propositions to true or false in a world.
 */
public final class Model {
    private final List<World> worlds;
    private final HashMap<World, List<World>> accessMap;
    private final HashMap<World, List<SingularFormula>> valuationMap;

    public Model(List<World> worlds, HashMap<World, List<World>> accessMap, HashMap<World, List<SingularFormula>> valuationMap) {
        this.worlds = worlds;
        this.accessMap = accessMap;
        this.valuationMap = valuationMap;
    }

    public List<World> getWorlds() {
        return Collections.unmodifiableList(worlds);
    }

    public List<World> worldsAccessibleFrom(World world) {
        return Collections.unmodifiableList(accessMap.get(world));
    }

    public List<SingularFormula> propositionsTrueAt(World world) {
        return Collections.unmodifiableList(valuationMap.get(world));
    }

    // TODO: implement a graphvis output function
}
