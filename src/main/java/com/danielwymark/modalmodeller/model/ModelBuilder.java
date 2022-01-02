package com.danielwymark.modalmodeller.model;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * Convenience class to ease the process of building a model.
 * The factory is mutable, but any given model is immutable once
 * it has been produced.
 */
public class ModelBuilder {
    private final Logger logger = Logger.getLogger(ModelBuilder.class);

    private int numWorlds;
    private final HashMap<Integer, Set<Integer>> tentativeAccessMap;
    private final HashMap<Integer, Set<String>> tentativeValuationMap;

    public ModelBuilder(int numWorlds) {
        this.numWorlds = numWorlds;
        tentativeAccessMap = new HashMap<>();
        tentativeValuationMap = new HashMap<>();
    }

    public ModelBuilder() {
        this(0);
    }

    public void setNumWorlds(int n) {
        numWorlds = n;
    }

    public void addRelation(int w1, int w2) {
        if (w1 >= numWorlds || w2 >= numWorlds) {
            numWorlds = Math.max(w1, w2);
            logger.info("Increasing number of worlds to " + numWorlds + " to account for relation (" + w1 + "," + w2 + ")");
        }

        if (tentativeAccessMap.containsKey(w1)) {
            tentativeAccessMap.get(w1).add(w2);
        } else {
            tentativeAccessMap.put(w1, new HashSet<>(Set.of(w2)));
        }
    }

    public void addFact(int w, String letter) {
        if (w >= numWorlds) {
            numWorlds = w;
            logger.info("Increasing number of worlds to " + numWorlds + " to account for fact: " + letter + " true at " + w);
        }

        if (tentativeValuationMap.containsKey(w)) {
            tentativeValuationMap.get(w).add(letter);
        } else {
            tentativeValuationMap.put(w, new HashSet<>(Set.of(letter)));
        }
    }

    public Model build() {
        var accessMap = new ArrayList<Set<Integer>>();
        var valuationMap = new ArrayList<Set<String>>();
        for (int i = 0; i < numWorlds; ++i) {
            // (1) Construct access map
            var neighbors = new HashSet<Integer>();
            if (tentativeAccessMap.containsKey(i)) {
                neighbors.addAll(tentativeAccessMap.get(i));
            }
            accessMap.add(neighbors);

            // (2) Construct valuation map
            var truths = new HashSet<String>();
            if (tentativeValuationMap.containsKey(i)) {
                truths.addAll(tentativeValuationMap.get(i));
            }
            valuationMap.add(truths);
        }

        return new Model(numWorlds, accessMap, valuationMap);
    }
}
