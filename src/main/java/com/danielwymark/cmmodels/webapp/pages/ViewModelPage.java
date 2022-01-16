package com.danielwymark.cmmodels.webapp.pages;

import com.danielwymark.cmmodels.core.exceptions.InvalidModelNumberError;
import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.ModelBuilder;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import io.javalin.http.Context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public record ViewModelPage(String modelNum, String imagesDirectory) implements Renderable {

    @Override
    public void render(Context ctx) {
        var outputPath = Path.of(imagesDirectory, modelNum + ".svg");
        var outputFile = new File(outputPath.toString());
        if (!outputFile.exists()) {
            Model model;
            try {
                model = ModelBuilder.buildFromModelNumber(modelNum);
            } catch (InvalidModelNumberError e) {
                ctx.render("ViewModel.jte", Map.of("modelNum", modelNum, "error", true));
                return;
            }

            var graph = model.generateGraph();
            try {
                Graphviz.fromGraph(graph).render(Format.SVG).toFile(outputFile);
            } catch (IOException e) {
                e.printStackTrace();
                ctx.render("ViewModel.jte", Map.of("modelNum", modelNum, "error", true));
                return;
            }
        }
        ctx.render("ViewModel.jte", Map.of("modelNum", modelNum, "error", false));
    }

}
