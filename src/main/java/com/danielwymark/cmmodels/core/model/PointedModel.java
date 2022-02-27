package com.danielwymark.cmmodels.core.model;

import com.danielwymark.cmmodels.core.exceptions.OutOfDomainError;
import guru.nidi.graphviz.model.Graph;

public class PointedModel extends Model {
    private final World pointedWorld;

    public PointedModel(Model model, World world) {
        super(model);
        if (!model.contains(world)) {
            throw new OutOfDomainError("World " + world + " not contained in model " + model);
        }
        pointedWorld = world;
    }

    @SuppressWarnings("unused")
    public World getPointedWorld() { return pointedWorld; }

    @Override
    public String modelNumber() {
        return "(" + modelNumber() + "," + pointedWorld.index() + ")";
    }

    @Override
    public Graph generateGraph() {
        return generateGraph((world, node) -> {
            if (world != pointedWorld)
                return node;
            return node.with("style", "filled")
                    .with("fillcolor", "gold")
                    .with("color", "green");
        });
    }
}
