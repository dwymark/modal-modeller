package com.danielwymark.cmmodels.relations;

import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.PointedModel;
import com.danielwymark.cmmodels.core.model.PointedRestrictedModelGenerator;
import com.danielwymark.cmmodels.core.relations.BisimulationSolver;
import com.danielwymark.cmmodels.core.relations.NaiveBisimulationSolver;
import com.danielwymark.cmmodels.core.relations.Relation;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BisimilarityTest {

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
