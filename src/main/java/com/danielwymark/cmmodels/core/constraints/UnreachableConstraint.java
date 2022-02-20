package com.danielwymark.cmmodels.core.constraints;

import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.World;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A pointed model M,s is called *unreachable* if there is no x in M such that xRs.
 */
public class UnreachableConstraint extends PointedModelConstraint {
    @Override
    protected boolean holdsOfImpl(Model model, World world) {
        // assume unreachable until proven otherwise
        AtomicBoolean unreachable = new AtomicBoolean(true);
        model.getWorlds().parallelStream().forEach(otherWorld -> {
            if (!unreachable.get())
                return;
            // if anyone sees world, it cannot be the root.
            if (model.accessible(otherWorld, world))
                unreachable.set(false);
        });
        return unreachable.get();
    }
}
