package com.danielwymark.cmmodels.core.model;

import com.danielwymark.cmmodels.core.exceptions.OutOfDomainError;

public record PointedModel(Model model, World world) {
    public PointedModel {
        if (!model.contains(world)) {
            throw new OutOfDomainError("World " + world + " not contained in model " + model);
        }
    }

    public String modelNumber() {
        return "(" + model.modelNumber() + "," + world.index() + ")";
    }
}
