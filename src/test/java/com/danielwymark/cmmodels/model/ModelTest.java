package com.danielwymark.cmmodels.model;

import com.danielwymark.cmmodels.exceptions.OutOfDomainError;
import com.danielwymark.cmmodels.syntax.AtomicFormula;
import org.junit.Assert;
import org.junit.Test;

public class ModelTest {

    public static Model getMockModel() {
        //
        //   _     3
        //  / \    |
        //  \ /    v
        //   0 --> 1 --> 2
        //    \_________/
        //
        //  V: M,0 |= {p, q, top};
        //     M,1 |= {p, top};
        //     M,2 |= {top};
        //     M,3 |= {q, top}

        final int NUM_WORLDS = 4;
        var modelBuilder = new ModelBuilder(NUM_WORLDS);
        modelBuilder.addRelation(0, 0);
        modelBuilder.addRelation(0, 1);
        modelBuilder.addRelation(0, 2);
        modelBuilder.addRelation(1, 2);
        modelBuilder.addRelation(3, 1);
        modelBuilder.addFact(0, "p");
        modelBuilder.addFact(0, "q");
        modelBuilder.addFact(1, "p");
        modelBuilder.addFact(3, "q");
        for (int i = 0; i < NUM_WORLDS; ++i) {
            modelBuilder.addFact(i, "top");
        }

        return modelBuilder.build();
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void ModelIsUnmodifiable() {
        var model = getMockModel();
        Assert.assertThrows(UnsupportedOperationException.class, () ->
                model.getWorlds().set(0, new World(3, 0)));
        Assert.assertThrows(UnsupportedOperationException.class, () ->
                model.worldsAccessibleFrom(model.getWorld(0))
                        .add(model.getWorld(1)));
        Assert.assertThrows(UnsupportedOperationException.class, () ->
                model.propositionsTrueAt(model.getWorld(0))
                        .add(new AtomicFormula("r")));
    }

    @Test
    public void WorldDomainEnforced() {
        var model = getMockModel();
        try {
            var w0 = model.getWorld(0);
            var w1 = model.getWorld(1);
            var w2 = model.getWorld(2);

            model.worldsAccessibleFrom(w0);
            model.worldsAccessibleFrom(w1);
            model.worldsAccessibleFrom(w2);

            model.propositionsTrueAt(w0);
            model.propositionsTrueAt(w1);
            model.propositionsTrueAt(w2);

            // Worlds are compared by equality of their index, so you can create a new world
            // object and pass it to member functions without going outside of domain if you
            // choose an appropriate index.

            model.worldsAccessibleFrom(0);
            model.worldsAccessibleFrom(1);
            model.worldsAccessibleFrom(2);

            model.propositionsTrueAt(0);
            model.propositionsTrueAt(1);
            model.propositionsTrueAt(2);
        }
        catch (OutOfDomainError e) {
            Assert.fail();
        }

        Assert.assertThrows(OutOfDomainError.class, () -> model.getWorld(100));
        Assert.assertThrows(OutOfDomainError.class, () -> model.worldsAccessibleFrom(100));
        Assert.assertThrows(OutOfDomainError.class, () -> model.propositionsTrueAt(100));
    }
}
