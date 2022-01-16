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
public class FactlessModelGenerator implements Generator<Model> {
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

            var modelBuilder = new ModelBuilder(numWorlds);

            // Every world W's accessibility relation given by a segment of the bitstream.
            // Each bit in the segment indicates whether W is related to a particular world.
            var mask = BigInteger.ONE;
            for (int i = 0; i < numWorlds; ++i) {
                for (int j = 0; j < numWorlds; ++j) {
                    if (mask.and(accessibilityBitString).compareTo(BigInteger.ZERO) != 0) {
                        modelBuilder.addRelation(i, j);
                    }
                    mask = mask.shiftLeft(1);
                }
            }

            accessibilityBitString = accessibilityBitString.add(BigInteger.ONE);
            return modelBuilder.build();
        };
        return Stream.generate(modelSupplier);
    }
}
