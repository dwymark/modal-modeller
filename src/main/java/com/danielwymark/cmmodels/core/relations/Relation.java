package com.danielwymark.cmmodels.core.relations;

import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.World;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableGraph;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static guru.nidi.graphviz.model.Factory.*;

public record Relation(Model left,
                       Model right,
                       Map<World, Set<World>> map) {
    // Could write a constructor that will verify that the worlds are in the models as expected, however that will incur
    // an O(n) performance cost to iterate over all the worlds in the map. Since the ultimate goal will be to generate a
    // very large number of these relations, I am refraining from adding this safety feature at this time.

    public Graph generateGraph() {
        Graph g = graph();
        Graph leftG = left.generateGraph().cluster();
        Graph rightG = right.generateGraph().cluster();
        g = g.with(leftG, rightG);

        Set<Set<World>> seen = new HashSet<>();
        for (var entry : map.entrySet()) {
            World source = entry.getKey();
            for (var target : entry.getValue()) {
                var pair = new HashSet<>(Set.of(source, target));
                if (!seen.contains(pair)) {
                    seen.add(pair);
                    g = g.with(node(source.toString()).link(to(node(target.toString())).with(Style.DOTTED)));
                }
            }
        }
        return g;
    }

    public boolean relates(World leftWorld, World rightWorld) {
        return map.get(leftWorld).contains(rightWorld);
    }

    @Override
    public String toString() {
        return "Relation{" + map + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relation relation = (Relation) o;
        return Objects.equals(left, relation.left) && Objects.equals(right, relation.right) && Objects.equals(map, relation.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right, map);
    }
}
