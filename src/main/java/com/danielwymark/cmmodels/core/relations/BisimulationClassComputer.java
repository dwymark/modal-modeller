package com.danielwymark.cmmodels.core.relations;

import com.danielwymark.cmmodels.core.model.PointedModel;
import com.danielwymark.cmmodels.core.model.PointedRestrictedModelGenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BisimulationClassComputer {
    private List<List<PointedModel>> bisimulationClasses = new ArrayList<>();
    private Iterator<PointedModel> pointedModelIterator = new PointedRestrictedModelGenerator().generate().iterator();
    private BisimulationSolver bisimulationSolver = new NaiveBisimulationSolver();
    private long numModelsAnalyzed;

    public List<List<PointedModel>> getBisimulationClasses() {
        return bisimulationClasses;
    }


    public long getNumModelsAnalyzed() {
        return numModelsAnalyzed;
    }

    public void analyzeNextModel() {
        PointedModel model = pointedModelIterator.next();
        for (List<PointedModel> bisimulationClass : bisimulationClasses) {
            PointedModel target = bisimulationClass.get(0);
            Relation bisimulation = bisimulationSolver.findLargestBisimulation(model, target);
            if (bisimulation.relates(model.getPointedWorld(), target.getPointedWorld())) {
                bisimulationClass.add(model);
                ++numModelsAnalyzed;
                return;
            }
        }
        PointedModel minimized = model.minimize();
        if (minimized.getNumWorlds() < model.getNumWorlds()) {
            bisimulationClasses.add(new ArrayList<>(List.of(minimized, model)));
        }
        else {
            bisimulationClasses.add(new ArrayList<>(List.of(model)));
        }
        ++numModelsAnalyzed;
    }
}