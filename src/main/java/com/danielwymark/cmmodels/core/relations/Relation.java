package com.danielwymark.cmmodels.core.relations;

import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.World;

import java.util.Map;

public record Relation(Model left,
                       Model right,
                       Map<World, World> map) {
    // Could write a constructor that will verify that the worlds are in the models as expected, however that will incur
    // an O(n) performance cost to iterate over all the worlds in the map. Since the ultimate goal will be to generate a
    // very large number of these relations, I am refraining from adding this safety feature at this time.

    // TODO: Implement GraphViz visualization for Relation


    @Override
    public String toString() {
        return "Relation{" + map + '}';
    }
}
