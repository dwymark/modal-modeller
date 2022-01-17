package com.danielwymark.cmmodels.webapp.pages;

import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.ModelBuilder;
import io.javalin.http.Context;

import java.math.BigInteger;
import java.util.Map;

public class CreateModelPage implements Renderable {
    private final String modelNum;
    private final Model model;

    // TODO: Handle the InvalidModelNumberErrors which can occur in the
    //       constructors for this page.

    public CreateModelPage(String modelNum) {
        this.modelNum = modelNum;
        this.model = ModelBuilder.buildFromModelNumber(modelNum);
    }

    public CreateModelPage(String modelNum, int numWorldsToAdd) {
        var modelBuilder = new ModelBuilder(modelNum);
        modelBuilder.setNumWorlds(modelBuilder.getNumWorlds() + numWorldsToAdd);
        model = modelBuilder.build();
        this.modelNum = model.modelNumber();
    }

    public CreateModelPage(String modelNum, Map<Integer, Integer> pairsToToggle) {
        var modelBuilder = new ModelBuilder(modelNum);
        for (var pair : pairsToToggle.entrySet()) {
            modelBuilder.toggleRelation(pair.getKey(), pair.getValue());
        }
        model = modelBuilder.build();
        this.modelNum = model.modelNumber();
    }

    @Override
    public void render(Context ctx) {
        ctx.render("CreateModel.jte", Map.of("modelNum", modelNum, "numWorlds", model.getNumWorlds()));
    }
}
