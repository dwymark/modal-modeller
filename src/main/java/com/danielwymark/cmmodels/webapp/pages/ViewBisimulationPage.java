package com.danielwymark.cmmodels.webapp.pages;

import com.danielwymark.cmmodels.core.exceptions.InvalidModelNumberError;
import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.ModelBuilder;
import com.danielwymark.cmmodels.core.relations.Block;
import com.danielwymark.cmmodels.core.relations.NaiveBisimulationSolver;
import com.danielwymark.cmmodels.core.relations.Relation;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import io.javalin.http.Context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public record ViewBisimulationPage(String leftModelNum, String rightModelNum, String imagesDirectory) implements Renderable {

    @Override
    public void render(Context ctx) {
        var outputPath = Path.of(imagesDirectory, leftModelNum + "+" + rightModelNum + ".svg");
        var outputFile = new File(outputPath.toString());

        Model leftModel, rightModel;
        try {
            leftModel = ModelBuilder.buildFromModelNumber(leftModelNum);
            rightModel = ModelBuilder.buildFromModelNumber(rightModelNum);
        } catch (InvalidModelNumberError e) {
            ctx.status(400);
            return;
        }

        var solver = new NaiveBisimulationSolver();
        if (!outputFile.exists()) {
            Relation bisimulation = solver.findLargestBisimulation(leftModel, rightModel);
            var graph = bisimulation.generateGraph();
            try {
                Graphviz.fromGraph(graph).render(Format.SVG).toFile(outputFile);
            } catch (IOException e) {
                ctx.status(400);
                return;
            }
        }

        List<Block> partitioning = solver.findCoarsestPartitioning(leftModel, rightModel);
        ctx.render("ViewBisimulation.jte", Map.of(
                "leftModelNum", leftModelNum,
                "rightModelNum", rightModelNum,
                "partitioning", partitioning.toString()));
    }
}
