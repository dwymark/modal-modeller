package com.danielwymark.cmmodels.core.paths;

import com.danielwymark.cmmodels.core.generator.Generator;
import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RootedPathGenerator implements Generator<WorldPath> {
    private final Model model;
    private final World world;

    public RootedPathGenerator(Model model, World world) {
        this.model = model;
        this.world = world;
    }

    private class RootedPathSpliterator implements Spliterator<WorldPath> {
        private final Stack<List<World>> paths;

        public RootedPathSpliterator() {
            paths = new Stack<>();
            paths.add(List.of(world));
        }

        public RootedPathSpliterator(Stack<List<World>> paths) {
            this.paths = paths;
        }

        @Override
        public boolean tryAdvance(Consumer<? super WorldPath> action) {
            if (paths.isEmpty())
                return false;
            List<World> path = paths.pop();
            World lastWorld = path.get(path.size() - 1);
            for (World neighbor : model.worldsAccessibleFrom(lastWorld)) {
                List<World> newPath = new ArrayList<>(path);
                newPath.add(neighbor);
                paths.push(newPath);
            }
            action.accept(new WorldPath(path));
            return true;
        }

        @Override
        public Spliterator<WorldPath> trySplit() {
            if (paths.size() <= 1)
                return null;
            var newStack = new Stack<List<World>>();
            for (int i = 0; i < paths.size() / 2; ++i) {
                newStack.push(paths.pop());
            }
            return new RootedPathSpliterator(newStack);
        }

        @Override
        public long estimateSize() {
            return Long.MAX_VALUE;
        }

        @Override
        public int characteristics() {
            return Spliterator.DISTINCT | Spliterator.NONNULL | Spliterator.IMMUTABLE;
        }
    }

    @Override
    public Stream<WorldPath> generate() {
//        Stack<List<World>> paths = new Stack<>();
//        paths.push(List.of(world));
//        AtomicBoolean emptyPathReturned = new AtomicBoolean(false);
//        Supplier<Path> pathSupplier = () -> {
//            if (emptyPathReturned.compareAndSet(false, true)) {
//                return new Path();
//            }
//            if (!paths.isEmpty()) {
//                List<World> path = paths.pop();
//                World lastWorld = path.get(path.size() - 1);
//                for (World neighbor : model.worldsAccessibleFrom(lastWorld)) {
//                    List<World> newPath = new ArrayList<>(path);
//                    newPath.add(neighbor);
//                    paths.push(newPath);
//                }
//                return new Path(path);
//            }
//            return null;
//        };
//        return Stream.generate(pathSupplier);
        return StreamSupport.stream(new RootedPathSpliterator(), true);
    }
}
