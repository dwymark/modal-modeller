package com.danielwymark.modalmodeller.app;

import com.danielwymark.modalmodeller.model.Model;
import com.danielwymark.modalmodeller.model.ModelBuilder;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;

import java.io.File;
import java.io.IOException;

public class ShellApp {
    public static void main(String[] args) {
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
}
