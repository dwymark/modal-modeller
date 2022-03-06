package com.danielwymark.cmmodels.relations;

import com.danielwymark.cmmodels.core.evaluation.Evaluator;
import com.danielwymark.cmmodels.core.evaluation.NaiveEvaluator;
import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.PointedModel;
import com.danielwymark.cmmodels.core.model.PointedRestrictedModelGenerator;
import com.danielwymark.cmmodels.core.model.RestrictedModelGenerator;
import com.danielwymark.cmmodels.core.relations.BisimulationSolver;
import com.danielwymark.cmmodels.core.relations.Block;
import com.danielwymark.cmmodels.core.relations.NaiveBisimulationSolver;
import com.danielwymark.cmmodels.core.relations.Relation;
import com.danielwymark.cmmodels.core.syntax.Formula;
import com.danielwymark.cmmodels.core.syntax.SequentialFormulaGenerator;
import org.junit.Assert;
import org.junit.Test;

import javax.sound.midi.SysexMessage;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BisimilarityTest {

    @Test
    public void BisimilarityRespectsInvarianceLemma() {
        final List<Formula> formulas = new SequentialFormulaGenerator().generate()
                .skip(100000) // only look at relatively complex formulas
                .limit(100).toList();
        final List<Model> models = new RestrictedModelGenerator().generate()
                .skip(100000) // only look at relatively large models
                .limit(100).toList();
        final Evaluator evaluator = new NaiveEvaluator();
        final NaiveBisimulationSolver bisimulationSolver = new NaiveBisimulationSolver();
        models.parallelStream().limit(500).forEach(model1 -> {
            models.parallelStream().skip(500).forEach(model2 -> {
                if (model1 == model2)
                    Assert.fail(); // this should be impossible
                Relation bisimulation = bisimulationSolver.findLargestBisimulation(model1, model2);
                var modelMap = Map.of(model1.getId(), model1, model2.getId(), model2);
                for (var keyval : bisimulation.map().entrySet()) {
                    var world = keyval.getKey();
                    for (var otherWorld : keyval.getValue()) {
                        var effectiveModel1 = modelMap.get(world.modelId());
                        var effectiveModel2 = modelMap.get(otherWorld.modelId());
                        for (var formula : formulas) {
                            if (evaluator.evaluate(effectiveModel1, world, formula)
                             != evaluator.evaluate(effectiveModel2, otherWorld, formula)) {
                                System.out.println("(" + model1.modelNumber() + "," + world.index() + ") ~ (" + model2.modelNumber() + "," + otherWorld.index() + ")");
                                System.out.println("Failed invariance lemma on " + formula);
                                Assert.fail();
                            }
                        }
                    }
                }
            });
        });
    }

    @Test
    public void PointedModelsDontBreakBisimulationSolver() {
        final BisimulationSolver bisimulationSolver = new NaiveBisimulationSolver();
        final Set<PointedModel> pointedModels = new PointedRestrictedModelGenerator().generate()
                .skip(1000000) // only look at relatively large models
                .limit(1000)
                .collect(Collectors.toSet());
        pointedModels.parallelStream().forEach(pointedModel1 -> {
            pointedModels.parallelStream().forEach(pointedModel2 -> {
                var plainModel1 = new Model(pointedModel1, false);
                var plainModel2 = new Model(pointedModel2, false);

                Relation pointedBisimulation = bisimulationSolver.findLargestBisimulation(pointedModel1, pointedModel2);
                Relation plainBisimulation = bisimulationSolver.findLargestBisimulation(plainModel1, plainModel2);

                for (int i = 0; i < plainModel1.getNumWorlds(); ++i) {
                    for (int j = 0; j < plainModel2.getNumWorlds(); ++j) {
                        boolean pointedRel = pointedBisimulation.relates(pointedModel1.getWorld(i), pointedModel2.getWorld(j));
                        boolean plainRel = plainBisimulation.relates(plainModel1.getWorld(i), plainModel2.getWorld(j));
                        if (pointedRel != plainRel) {
                            System.out.println(pointedModel1.modelNumber() + " world " + pointedModel1.getWorld(i) + " bisimilar to "
                                    + pointedModel2.modelNumber() + " world " + pointedModel2.getWorld(j) + ": " + pointedRel);
                            System.out.println(plainModel1.modelNumber() + " world " + plainModel1.getWorld(i) + " bisimilar to "
                                    + plainModel2.modelNumber() + " world " + plainModel2.getWorld(j) + ": " + plainRel);
                            System.out.println(pointedBisimulation);
                            System.out.println(plainBisimulation);
                            Assert.fail();
                        }
                    }
                }
            });
        });
    }

}
