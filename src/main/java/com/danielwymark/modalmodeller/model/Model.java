package com.danielwymark.modalmodeller.model;

import com.danielwymark.modalmodeller.exceptions.OutOfDomainError;
import com.danielwymark.modalmodeller.syntax.AtomicFormula;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.attribute.Rank.RankDir.TOP_TO_BOTTOM;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

/**
 * Represents a possible worlds model, which is defined by a set of worlds W,
 * an accessibility relation on worlds R, and a valuation map V(s, p) which
 * maps propositions to true or false in a world.
 */
public final class Model {
    private final World[] worlds;
    private final Set<World>[] accessMap;
    private final Set<AtomicFormula>[] valuationMap;
    private final int numWorlds;
    private final long identifier;
    private static final AtomicLong nextModelNum = new AtomicLong(0);

    @SuppressWarnings("unchecked") // array of sets
    public Model(int numWorlds, List<Set<Integer>> accessMap, List<Set<String>> valuationMap) {
        identifier = nextModelNum.getAndIncrement();
        this.numWorlds = numWorlds;

        worlds = new World[numWorlds];
        for (int i = 0; i < numWorlds; ++i) {
            worlds[i] = new World(i, identifier);
        }


        this.accessMap = new Set[numWorlds];
        for (int i = 0; i < numWorlds; ++i) {
            this.accessMap[i] = new HashSet<>();
            for (var worldIdx : accessMap.get(i)) {
                this.accessMap[i].add(worlds[worldIdx]);
            }
        }

        this.valuationMap = new Set[numWorlds];
        for (int i = 0; i < numWorlds; ++i) {
            this.valuationMap[i] = new HashSet<>();
            for (var letter : valuationMap.get(i)) {
                this.valuationMap[i].add(new AtomicFormula(letter));
            }
        }
    }

    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * Accessors
     */

    public List<World> getWorlds() {
        return List.of(worlds);
    }

    public World getWorld(int i) throws OutOfDomainError {
        assertInDomain(i);
        return worlds[i];
    }

    public int getNumWorlds() {
        return numWorlds;
    }

    //--

    public Set<World> worldsAccessibleFrom(int world) {
        assertInDomain(world);
        return Collections.unmodifiableSet(accessMap[world]);
    }

    public Set<World> worldsAccessibleFrom(World world) {
        return worldsAccessibleFrom(world.index());
    }

    //--

    public Set<AtomicFormula> propositionsTrueAt(int world) {
        assertInDomain(world);
        return Collections.unmodifiableSet(valuationMap[world]);
    }

    public Set<AtomicFormula> propositionsTrueAt(World world) {
        return propositionsTrueAt(world.index());
    }

    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * Visualization
     */

    public Graph generateGraph() {
        Graph graph = graph("Model " + hashCode()).directed()
                .graphAttr().with(Rank.dir(LEFT_TO_RIGHT))
                .nodeAttr().with(Font.name("sans-serif"))
                .linkAttr().with("class", "link-class");

        for (World world : worlds) {
            var truePropositions = new ArrayList<String>();
            for (AtomicFormula formula : propositionsTrueAt(world)) {
                truePropositions.add(formula.letter);
            }

            var label = Integer.toString(world.index());
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
        if (i >= numWorlds)
            throw new OutOfDomainError("World " + i + " not in domain of model");
    }

    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * Standard overrides
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return identifier == model.identifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}
