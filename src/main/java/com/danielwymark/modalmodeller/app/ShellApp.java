package com.danielwymark.modalmodeller.app;

import com.danielwymark.modalmodeller.model.Model;
import com.danielwymark.modalmodeller.model.ModelBuilder;
import com.danielwymark.modalmodeller.model.FactlessModelGenerator;
import com.danielwymark.modalmodeller.relations.NaiveBisimulationSolver;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class ShellApp {
    private void testGraphvizSingleModel() {
        final int NUM_WORLDS = 4;
        var modelBuilder = new ModelBuilder(NUM_WORLDS);
        modelBuilder.addRelation(0, 0);
        modelBuilder.addRelation(0, 1);
        modelBuilder.addRelation(0, 2);
        modelBuilder.addRelation(1, 2);
        modelBuilder.addRelation(3, 1);
        modelBuilder.addFact(0, "p");
        modelBuilder.addFact(0, "q");
        modelBuilder.addFact(1, "p");
        modelBuilder.addFact(3, "q");

        Model model = modelBuilder.build();
        try {
            Graphviz.fromGraph(model.generateGraph())
                    .height(600).render(Format.PNG).toFile(new File("model.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateFactlessModels() {
        final AtomicInteger i = new AtomicInteger(-1);
        new FactlessModelGenerator().generate()
                .limit(512+16+2)
                .forEach((Model model) -> {
                    try {
                        Graphviz.fromGraph(model.generateGraph())
                                .height(600).render(Format.PNG).toFile(new File("model" + i.addAndGet(1) + ".png"));
                        System.out.println(i);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(-1);
                    }
                });
    }



    public static void main(String[] args) {
        var solver = new NaiveBisimulationSolver();
        var modelBuilder = new ModelBuilder(4);
        modelBuilder.addRelation(0, 1);
        modelBuilder.addRelation(0, 2);
        modelBuilder.addRelation(2, 3);
        Model model1 = modelBuilder.build();

        modelBuilder = new ModelBuilder(3);
        modelBuilder.addRelation(0, 1);
        modelBuilder.addRelation(0, 2);
        modelBuilder.addRelation(1, 2);
        Model model2 = modelBuilder.build();

        System.out.println(solver.findLargestBisimulation(model1, model2));
    }
}
