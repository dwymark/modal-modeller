package com.danielwymark.cmmodels.core.model;

import com.danielwymark.cmmodels.core.generator.Generator;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class TreeGenerator implements Generator<PointedModel> {

    // this is broken
    @Override
    public Stream<PointedModel> generate() {
        Set<PointedModel> seen = new HashSet<>();
        var frontier = new ArrayDeque<PointedModel>();
        frontier.push((PointedModel) ModelBuilder.buildFromModelNumber("(1w0,0)"));
        Supplier<PointedModel> supplier = () -> {
            var current = frontier.removeFirst();
            ModelBuilder modelBuilder = new ModelBuilder(current.modelNumber());
            seen.add(current);
            for (PointedModel model : seen) {
                var nextModelBuilder = new ModelBuilder(modelBuilder);
                nextModelBuilder.addModel(model);
                frontier.add((PointedModel) nextModelBuilder.build());
            }
            seen.clear();
            return current;
        };
        return Stream.generate(supplier);
    }


//    @SuppressWarnings("unchecked")
//    @Override
//    public Stream<PointedModel> generate() {
//        List<Set<Object>> seen = new ArrayList<>();
//        var frontier = new ArrayDeque<Set<Object>>();
//        frontier.push(Set.of());
//        Supplier<PointedModel> supplier = () -> {
//            ModelBuilder modelBuilder = new ModelBuilder();
//            var current = frontier.removeFirst();
//            for (var set : seen) {
//                var next = new HashSet<>(current);
//                next.add(set);
//                frontier.push(next);
//            }
//
//            modelBuilder.setNumWorlds(current.size());
//            int i = 0;
//            for (var setObj : current) {
//                Set<Object> set = (Set<Object>) setObj;
//                for (var other : set) {
//                    modelBuilder.addRelation(i, );
//                }
//            }
//
//            seen.add(current);
//            return current;
//        };
//        return Stream.generate(supplier);
//    }
//
//    private PointedModel generateModel(Set<Object> labeling) {
//        ModelBuilder modelBuilder = new ModelBuilder();
//
//    }
//
//    private int getWorldIndex(Set<Object> labeling) {
//        if (labeling.isEmpty())
//            return 0;
//
//    }
}
