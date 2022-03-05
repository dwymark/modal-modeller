package com.danielwymark.cmmodels.core.model;

import com.danielwymark.cmmodels.core.exceptions.OutOfDomainError;
import com.danielwymark.cmmodels.core.relations.Block;
import guru.nidi.graphviz.model.Graph;

import java.util.concurrent.atomic.AtomicInteger;

public class PointedModel extends Model {
    private final World pointedWorld;

    public PointedModel(Model model, World world) {
        super(model, true);
        if (!model.contains(world)) {
            throw new OutOfDomainError("World " + world + " not contained in model " + model);
        }
        pointedWorld = world;
    }

    @SuppressWarnings("unused")
    public World getPointedWorld() { return pointedWorld; }

    @Override
    public String modelNumber() {
        return "(" + super.modelNumber() + "," + pointedWorld.index() + ")";
    }

    @Override
    public Graph generateGraph() {
        return generateGraph((world, node) -> {
            if (world != pointedWorld)
                return node;
            return node.with("style", "filled")
                    .with("fillcolor", "gold");
        });
    }

    @Override
    public PointedModel minimize() {
        AtomicInteger newPoint = new AtomicInteger(-1);
        Model minimized = super.minimize((Block block, int idx) -> {
            if (block.worlds().contains(pointedWorld)) {
                newPoint.set(idx);
            }
        });
        if (newPoint.get() < 0 || newPoint.get() >= numWorlds) {
            throw new IllegalStateException("This should be impossible");
        }
        return new PointedModel(minimized, minimized.getWorld(newPoint.get()));
    }
}
