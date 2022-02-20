package com.danielwymark.cmmodels.core.constraints;

import com.danielwymark.cmmodels.core.exceptions.OutOfDomainError;
import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.World;

public abstract class PointedModelConstraint {
    protected abstract boolean holdsOfImpl(Model model, World world);

    public boolean holdsOf(Model model, World world) {
        if (!model.contains(world)) {
            throw new OutOfDomainError("Can't evaluate constraint on invalid pointed model.");
        }
        return holdsOfImpl(model, world);
    }

    public boolean holdsOf(Model model, int world) {
        if (world >= model.getNumWorlds()) {
            throw new OutOfDomainError("Can't make pointed model for index " + world + " on model of size " + model.getNumWorlds());
        }
        return holdsOfImpl(model, model.getWorld(world));
    }
}
