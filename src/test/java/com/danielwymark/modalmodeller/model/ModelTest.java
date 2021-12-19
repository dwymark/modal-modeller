package com.danielwymark.modalmodeller.model;

import com.danielwymark.modalmodeller.exceptions.OutOfDomainError;
import com.danielwymark.modalmodeller.syntax.SingularFormula;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

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

        var letters = new LinkedList<>(List.of(
                new SingularFormula("p"),
                new SingularFormula("q"),
                new SingularFormula("top")
        ));

        var worlds = new LinkedList<>(List.of(
                new World(0), new World(1), new World(2), new World(3)));

        var accessMap = new HashMap<>(Map.of(
                worlds.get(0), new HashSet<>(Set.of(
                        worlds.get(0), worlds.get(1), worlds.get(2))),
                worlds.get(1), new HashSet<>(Set.of(worlds.get(2))),
                worlds.get(2), new HashSet<World>(),
                worlds.get(3), new HashSet<>(Set.of(worlds.get(1)))
        ));

        var valuationMap = new HashMap<>(Map.of(
                worlds.get(0), new HashSet<>(Set.of(letters.get(0), letters.get(1), letters.get(2))),
                worlds.get(1), new HashSet<>(Set.of(letters.get(0), letters.get(2))),
                worlds.get(2), new HashSet<>(Set.of(letters.get(2))),
                worlds.get(3), new HashSet<>(Set.of(letters.get(1), letters.get(2)))
        ));

        return new Model(worlds, accessMap, valuationMap);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void ModelIsUnmodifiable() {
        var model = getMockModel();
        Assert.assertThrows(UnsupportedOperationException.class, () ->
                model.getWorlds().set(0, new World(3)));
        Assert.assertThrows(UnsupportedOperationException.class, () ->
                model.worldsAccessibleFrom(model.getWorld(0))
                        .add(model.getWorld(1)));
        Assert.assertThrows(UnsupportedOperationException.class, () ->
                model.propositionsTrueAt(model.getWorld(0))
                        .add(new SingularFormula("r")));
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

            model.worldsAccessibleFrom(new World(0));
            model.worldsAccessibleFrom(new World(1));
            model.worldsAccessibleFrom(new World(2));

            model.propositionsTrueAt(new World(0));
            model.propositionsTrueAt(new World(1));
            model.propositionsTrueAt(new World(2));
        }
        catch (OutOfDomainError e) {
            Assert.fail();
        }

        Assert.assertThrows(OutOfDomainError.class, () -> model.getWorld(100));
        Assert.assertThrows(OutOfDomainError.class, () -> model.worldsAccessibleFrom(new World(100)));
        Assert.assertThrows(OutOfDomainError.class, () -> model.propositionsTrueAt(new World(100)));
    }
}
