package com.danielwymark.cmmodels.core.model;

import com.danielwymark.cmmodels.core.exceptions.GeneratorExhaustedError;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Generates all possible models with no propositions true at any world.
 * Isomorphic copies will occur in the output -- all possible accessibility relations are included.
 */
public class RestrictedModelGenerator implements Generator<Model> {
    private int numWorlds = 1;
    private BigInteger accessibilityBitString = BigInteger.valueOf(0);
    private BigInteger accessibilityBitStringMax = BigInteger.valueOf(2);
    private final AtomicBoolean started = new AtomicBoolean(false);

    private void calculateBitstreamMax() {
        accessibilityBitStringMax = BigInteger.TWO.pow(numWorlds * numWorlds);
    }

    @Override
    public Stream<Model> generate() {
        if (!started.compareAndSet(false, true)) {
            throw new GeneratorExhaustedError("Any instance of ModelGenerator can only generate a single stream.");
        }
        Supplier<Model> modelSupplier = () -> {
            if (accessibilityBitString.compareTo(accessibilityBitStringMax) >= 0) {
                numWorlds++;
                calculateBitstreamMax();
                accessibilityBitString = BigInteger.valueOf(0);
            }
            var model = ModelBuilder.buildFromModelNumber(numWorlds + "w" + accessibilityBitString);
            accessibilityBitString = accessibilityBitString.add(BigInteger.ONE);
            return model;
        };
        return Stream.generate(modelSupplier);
    }
}
