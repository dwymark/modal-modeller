package com.danielwymark.cmmodels.paths;

import com.danielwymark.cmmodels.core.model.ModelBuilder;
import com.danielwymark.cmmodels.core.paths.RootedPathGenerator;
import com.danielwymark.cmmodels.core.paths.WorldPath;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PathTest {
    @Test
    public void FiniteModelsHaveExactlyCorrectNumberOfPaths() {
        var modelBuilder = new ModelBuilder(1);
        var model = modelBuilder.build();
        var pathGenerator = new RootedPathGenerator(model, model.getWorld(0));
        var paths = pathGenerator.generate().collect(Collectors.toList());
        Assert.assertEquals(paths.size(), 1); // just the empty path

        modelBuilder.addRelation(0, 1);
        model = modelBuilder.build();
        pathGenerator = new RootedPathGenerator(model, model.getWorld(0));
        paths = pathGenerator.generate().collect(Collectors.toList());
        Assert.assertEquals(paths.size(), 2); // (0), (0, 1)

        modelBuilder.addRelation(0, 2);
        modelBuilder.addRelation(1, 2);
        model = modelBuilder.build();
        pathGenerator = new RootedPathGenerator(model, model.getWorld(0));
        paths = pathGenerator.generate().collect(Collectors.toList());
        // (0), (0, 1), (0, 1, 2), (0, 2)
        Assert.assertEquals(paths.size(), 4);
    }

    @Test
    public void InfiniteModelsHaveExpectedPaths() {
        var model = ModelBuilder.buildFromModelNumber("1w1");
        var pathGenerator = new RootedPathGenerator(model, model.getWorld(0));
        var paths = pathGenerator.generate().limit(4).collect(Collectors.toList());
        var world = model.getWorld(0);
        Assert.assertTrue(paths.contains(new WorldPath(List.of(world))));
        Assert.assertTrue(paths.contains(new WorldPath(List.of(world, world))));
        Assert.assertTrue(paths.contains(new WorldPath(List.of(world, world, world))));
        Assert.assertTrue(paths.contains(new WorldPath(List.of(world, world, world, world))));

        var modelBuilder = new ModelBuilder(2);
        modelBuilder.addRelation(0, 1);
        modelBuilder.addRelation(1, 0);
        model = modelBuilder.build();
        pathGenerator = new RootedPathGenerator(model, model.getWorld(0));
        paths = pathGenerator.generate().limit(4).collect(Collectors.toList());
        world = model.getWorld(0);
        var otherWorld = model.getWorld(1);
        Assert.assertTrue(paths.contains(new WorldPath(List.of(world))));
        Assert.assertTrue(paths.contains(new WorldPath(List.of(world, otherWorld))));
        Assert.assertTrue(paths.contains(new WorldPath(List.of(world, otherWorld, world))));
        Assert.assertTrue(paths.contains(new WorldPath(List.of(world, otherWorld, world, otherWorld))));
    }
}
