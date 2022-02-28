package com.danielwymark.cmmodels.core.model;

import com.danielwymark.cmmodels.core.exceptions.GeneratorExhaustedError;
import com.danielwymark.cmmodels.core.generator.Generator;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class PointedRestrictedModelGenerator implements Generator<PointedModel> {
    @Override
    public Stream<PointedModel> generate() {
        RestrictedModelGenerator restrictedModelGenerator = new RestrictedModelGenerator();
        Stream<Model> modelStream = restrictedModelGenerator.generate();
        Iterator<Model> modelIterator = modelStream.iterator();

        AtomicInteger currentWorldIndex = new AtomicInteger(0);
        AtomicReference<Model> currentModel = new AtomicReference<>(modelIterator.next());
        Supplier<PointedModel> modelSupplier = () -> {
            if (currentWorldIndex.get() >= currentModel.get().numWorlds) {
                currentModel.set(modelIterator.next());
                currentWorldIndex.set(0);
            }
            return new PointedModel(currentModel.get(), currentModel.get().getWorld(currentWorldIndex.getAndIncrement()));
        };
        return Stream.generate(modelSupplier);
    }
}
