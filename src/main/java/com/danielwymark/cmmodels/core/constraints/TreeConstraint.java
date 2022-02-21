package com.danielwymark.cmmodels.core.constraints;

import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.World;
import com.danielwymark.cmmodels.core.paths.RootedPathGenerator;
import com.danielwymark.cmmodels.core.paths.WorldPath;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A pointed model M,s is a tree iff M is acyclic, s is unreachable, all paths from any world A to world B are unique,
 * and all worlds are reachable from the root s.
 */
public class TreeConstraint extends PointedModelConstraint {
    @Override
    protected boolean holdsOfImpl(Model model, World world) {
        if (!(ConstraintUtil.isAcyclic(model) && ConstraintUtil.isUnreachable(model, world)))
            return false;

        var pathGenerator = new RootedPathGenerator(model, world);
        List<WorldPath> paths = pathGenerator.generate().collect(Collectors.toList());

        Set<List<World>> pathDescriptions = new HashSet<>();
        for (WorldPath path : paths) {
            List<World> pathDescription = List.of(path.getFirst(), path.getLast());
            if (pathDescriptions.contains(pathDescription)) {
                return false; // more than one way to get from A to B
            }
            pathDescriptions.add(pathDescription);
        }

        Set<World> reachable = pathDescriptions.stream().map(desc -> desc.get(1)).collect(Collectors.toSet());
        for (World w : model.getWorlds()) {
            if (!reachable.contains(w)) // no path to w
                return false;
        }
        return true;
    }
}
