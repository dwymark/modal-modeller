package com.danielwymark.cmmodels.core.syntax;

import com.danielwymark.cmmodels.core.generator.Generator;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SequentialFormulaGenerator implements Generator<Formula> {
    private final List<AtomicFormula> atoms;
    private final int limit; // lower limit biases towards higher modal depth

    public SequentialFormulaGenerator(Set<AtomicFormula> atoms, int limit) {
        atoms.add(AtomicFormula.BOTTOM);
        atoms.add(AtomicFormula.TOP);
        this.limit = limit;
        this.atoms = atoms.stream().toList();
    }

    public SequentialFormulaGenerator(int limit) {
        this(new HashSet<>(), limit);
    }

    public SequentialFormulaGenerator() {
        this(new HashSet<>(), 500);
    }

    @Override
    public Stream<Formula> generate() {
        List<Formula> seen = new ArrayList<>();
        List<Formula> seeds = new ArrayList<>(atoms);
        Supplier<Formula> formulaSupplier = () -> {
            var seed = seeds.get(0);
            seeds.remove(0);
            List<Formula> newSeeds = new ArrayList<>(modalCombinations(seed));
            if (seeds.size() < limit) {
                for (var other : seen) {
                    newSeeds.addAll(booleanCombinations(seed, other));
                }
            }
            seen.add(seed);
            seeds.addAll(newSeeds);
            return seed;
        };
        return Stream.generate(formulaSupplier);
    }

    private List<Formula> modalCombinations(Formula formula) {
        return List.of(
                Formula.might(formula),
                Formula.must(formula),
                Formula.negate(Formula.might(formula)),
                Formula.negate(Formula.must(formula))
        );
    }

    private List<Formula> booleanCombinations(Formula formula1, Formula formula2) {
        var flipped1 = formula1.flipPolarity();
        var flipped2 = formula2.flipPolarity();
        return List.of(
                formula1.and(formula2),
                formula1.or(formula2)//,
//                flipped1.and(formula2),
//                flipped1.or(formula2),
//                formula1.and(flipped2),
//                formula1.or(flipped2),
//                flipped1.and(flipped2),
//                flipped2.or(flipped2)
        );
    };
}
