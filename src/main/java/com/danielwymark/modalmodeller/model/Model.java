package com.danielwymark.modalmodeller.model;

import com.danielwymark.modalmodeller.exceptions.OutOfDomainError;
import com.danielwymark.modalmodeller.syntax.SingularFormula;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.util.*;

import static guru.nidi.graphviz.attribute.Attributes.attr;
import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.attribute.Rank.RankDir.TOP_TO_BOTTOM;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static guru.nidi.graphviz.model.Link.to;

/**
 * Represents a possible worlds model, which is defined by a set of worlds W,
 * an accessibility relation on worlds R, and a valuation map V(s, p) which
 * maps propositions to true or false in a world.
 */
public final class Model {
    private final List<World> worlds;
    private final Map<World, HashSet<World>> accessMap;
    private final Map<World, HashSet<SingularFormula>> valuationMap;
    private final HashMap<Integer, World> worldIndexMap;

    public Model(List<World> worlds, Map<World, HashSet<World>> accessMap,
                 Map<World, HashSet<SingularFormula>> valuationMap) {
        this.worlds = worlds;
        worldIndexMap = new HashMap<>();
        for (var world : worlds) {
            worldIndexMap.put(world.index, world);
        }
        this.accessMap = accessMap;
        this.valuationMap = valuationMap;
    }

    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * Accessors
     */

    public List<World> getWorlds() {
        return Collections.unmodifiableList(worlds);
    }

    public World getWorld(int i) throws OutOfDomainError {
        assertInDomain(i);
        return worldIndexMap.get(i);
    }

    public Set<World> worldsAccessibleFrom(World world) {
        assertInDomain(world.index);
        return Collections.unmodifiableSet(accessMap.get(world));
    }

    public Set<SingularFormula> propositionsTrueAt(World world) {
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

    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * Visualization
     */

    public Graph generateGraph() {
        Graph graph = graph("Model " + hashCode()).directed()
                .graphAttr().with(Rank.dir(TOP_TO_BOTTOM))
                .nodeAttr().with(Font.name("sans-serif"))
                .linkAttr().with("class", "link-class");

        for (World world : worlds) {
            var truePropositions = new ArrayList<String>();
            for (SingularFormula formula : propositionsTrueAt(world)) {
                truePropositions.add(formula.letter);
            }

            var label = Integer.toString(world.index);
            if (truePropositions.size() > 0) {
                label += "\n" + Arrays.toString(truePropositions.stream().sorted().toArray());
            }

            Node worldNode = node(world.toString()).with("label", label);
            for (World neighbor : worldsAccessibleFrom(world)) {
                worldNode = worldNode.link(neighbor.toString());
            }
            graph = graph.with(worldNode);
        }

        return graph;
    }

    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * Helper functions
     */

    private void assertInDomain(int i) throws OutOfDomainError {
        if (!worldIndexMap.containsKey(i))
            throw new OutOfDomainError("World " + i + " not in domain of model");
    }

    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * Standard overrides
     */

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

}
