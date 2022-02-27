package com.danielwymark.cmmodels.core.model;

import com.danielwymark.cmmodels.core.exceptions.OutOfDomainError;
import com.danielwymark.cmmodels.core.relations.Block;
import com.danielwymark.cmmodels.core.relations.NaiveBisimulationSolver;
import com.danielwymark.cmmodels.core.syntax.AtomicFormula;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static guru.nidi.graphviz.attribute.Rank.RankDir.LEFT_TO_RIGHT;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;

/**
 * Represents a possible worlds model, which is defined by a set of worlds W,
 * an accessibility relation on worlds R, and a valuation map V(s, p) which
 * maps propositions to true or false in a world.
 */
public class Model {
    protected final World[] worlds;
    protected final Set<World>[] accessMap;
    protected final Set<AtomicFormula>[] valuationMap;
    private final int numWorlds;
    private final long id;
    private static final AtomicLong nextModelNum = new AtomicLong(0);

    @SuppressWarnings("unchecked") // suppress warning arising from making an array of sets
    public Model(int numWorlds, List<Set<Integer>> accessMap, List<Set<String>> valuationMap) {
        id = nextModelNum.getAndIncrement();
        this.numWorlds = numWorlds;

        worlds = new World[numWorlds];
        for (int i = 0; i < numWorlds; ++i) {
            worlds[i] = new World(i, id);
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

    public Model(Model model) {
        worlds = model.worlds;
        accessMap = model.accessMap;
        valuationMap = model.valuationMap;
        numWorlds = model.numWorlds;
        id = nextModelNum.getAndIncrement();
    }

    public String modelNumber() {
        var accessibilityRelation = BigInteger.ZERO;
        var mask = BigInteger.ONE;
        for (int i = 0; i < numWorlds; ++i) {
            for (int j = 0; j < numWorlds; ++j) {
                if (accessible(i,j)) {
                    accessibilityRelation = accessibilityRelation.or(mask);
                }
                mask = mask.shiftLeft(1);
            }
        }
        return numWorlds + "w" + accessibilityRelation;
    }

    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * Accessors
     */

    public long getId() {
        return id;
    }

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

    public boolean contains(World world) {
        int i = world.index();
        return i < numWorlds && worlds[i] == world;
    }

    //--

    public Set<World> worldsAccessibleFrom(int world) {
        assertInDomain(world);
        return Collections.unmodifiableSet(accessMap[world]);
    }

    public Set<World> worldsAccessibleFrom(World world) {
        if (world.modelId() != id)
            return new HashSet<>();
        return worldsAccessibleFrom(world.index());
    }

    public boolean accessible(World sourceWorld, World targetWorld) {
        return worldsAccessibleFrom(sourceWorld).contains(targetWorld);
    }

    public boolean accessible(int sourceWorld, int targetWorld) {
        return worldsAccessibleFrom(sourceWorld).contains(getWorld(targetWorld));
    }

    //--

    public Set<AtomicFormula> propositionsTrueAt(int world) {
        assertInDomain(world);
        return Collections.unmodifiableSet(valuationMap[world]);
    }

    public Set<AtomicFormula> propositionsTrueAt(World world) {
        if (world.modelId() != id)
            return new HashSet<>();
        return propositionsTrueAt(world.index());
    }

    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * Operations
     */

    public Model minimize() {
        var solver = new NaiveBisimulationSolver();
        var partitioning = solver.findCoarsestPartitioning(this, this);
        var modelBuilder = new ModelBuilder(partitioning.size());
        for (int i = 0; i < partitioning.size(); ++i) {
            Block block = partitioning.get(i);
            World representative = block.worlds().stream().findFirst().orElse(null);
            if (representative == null)
                throw new IllegalStateException("This should be impossible");
            for (World world : worldsAccessibleFrom(representative)) {
                for (int j = 0; j < partitioning.size(); ++j) {
                    if (partitioning.get(j).worlds().contains(world)) {
                        modelBuilder.addRelation(i, j);
                        break;
                    }
                }
            }
        }
        return modelBuilder.build();
    }


    /*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
     * Visualization
     */

    public Graph generateGraph(WorldColoring coloring) {
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
            worldNode = coloring.color(world, worldNode);

            for (World neighbor : worldsAccessibleFrom(world)) {
                worldNode = worldNode.link(neighbor.toString());
            }
            graph = graph.with(worldNode);
        }

        return graph;
    }

    public Graph generateGraph() {
        return generateGraph((world, worldNode) -> worldNode);
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
        return id == model.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Model{" +
                "numWorlds=" + numWorlds +
                ", id=" + id +
                '}';
    }
}
