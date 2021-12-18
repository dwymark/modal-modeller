package com.danielwymark.modalmodeller.model;

import com.danielwymark.modalmodeller.syntax.SingularFormula;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class ModelTest {

    public Model getTestModel() {
        //   _
        //  / \
        //  \ /
        //   0 --> 1 --> 2
        //    \_________/
        //
        //  V: M,0 |= {p, q}; M,1 |= {p}; M,2 |= {}

        var letters = new LinkedList<>(List.of(
                new SingularFormula("p"),
                new SingularFormula("q")
        ));

        var worlds = new LinkedList<>(List.of(
                new World(0), new World(1), new World(2)));

        var accessMap = new HashMap<>(Map.of(
                worlds.get(0), new HashSet<>(Set.of(
                        worlds.get(0), worlds.get(1), worlds.get(2))),
                worlds.get(1), new HashSet<>(Set.of(worlds.get(2))),
                worlds.get(2), new HashSet<World>()
        ));

        var valuationMap = new HashMap<>(Map.of(
                worlds.get(0), new HashSet<>(Set.of(letters.get(0), letters.get(1))),
                worlds.get(1), new HashSet<>(Set.of(letters.get(0))),
                worlds.get(2), new HashSet<SingularFormula>()
        ));

        return new Model(worlds, accessMap, valuationMap);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void ModelIsUnmodifiable() {
        var model = getTestModel();
        Assert.assertThrows(UnsupportedOperationException.class, () ->
                model.getWorlds().set(0, new World(3)));
        Assert.assertThrows(UnsupportedOperationException.class, () ->
                model.worldsAccessibleFrom(model.getWorld(0))
                        .add(model.getWorld(1)));
        Assert.assertThrows(UnsupportedOperationException.class, () ->
                model.propositionsTrueAt(model.getWorld(0))
                        .add(new SingularFormula("r")));
    }
}
