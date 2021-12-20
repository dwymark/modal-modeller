package com.danielwymark.modalmodeller.model;

import com.danielwymark.modalmodeller.syntax.SingularFormula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Convenience class to ease the process of building a model.
 * The factory is mutable, but any given model is immutable once
 * it has been produced.
 */
public class ModelFactory {
    private int numWorlds;
    private final HashMap<Integer, HashSet<Integer>> tentativeAccessMap;
    private final HashMap<Integer, HashSet<String>> tentativeValuationMap;

    public ModelFactory(int numWorlds) {
        this.numWorlds = numWorlds;
        tentativeAccessMap = new HashMap<>();
        tentativeValuationMap = new HashMap<>();
    }

    void setNumWorlds(int n) {
        numWorlds = n;
    }

    void addRelation(int w1, int w2) {
        if (tentativeAccessMap.containsKey(w1)) {
            tentativeAccessMap.get(w1).add(w2);
        }
        else {
            tentativeAccessMap.put(w1, new HashSet<>(Set.of(w2)));
        }
    }

    void addTruth(int w, String letter) {
        if (tentativeValuationMap.containsKey(w)) {
            tentativeValuationMap.get(w).add(letter);
        }
        else {
            tentativeValuationMap.put(w, new HashSet<>(Set.of(letter)));
        }
    }

    Model build() {
        // (1) Construct world list
        var worlds = new ArrayList<World>();
        for (int i = 0; i < numWorlds; ++i) {
            worlds.add(new World(i));
        }

        var accessMap = new HashMap<World, HashSet<World>>();
        var valuationMap = new HashMap<World, HashSet<SingularFormula>>();
        for (int i = 0; i < numWorlds; ++i) {
            // (2) Construct access map
            var neighbors = new HashSet<World>();
            if (tentativeAccessMap.containsKey(i)) {
                for (int neighborIdx : tentativeAccessMap.get(i)) {
                    neighbors.add(worlds.get(neighborIdx));
                }
            }
            accessMap.put(worlds.get(i), neighbors);

            // (3) Construct valuation map
            var truths = new HashSet<SingularFormula>();
            if (tentativeValuationMap.containsKey(i)) {
                for (String letter : tentativeValuationMap.get(i)) {
                    truths.add(new SingularFormula(letter));
                }
            }
            valuationMap.put(worlds.get(i), truths);
        }

        return new Model(worlds, accessMap, valuationMap);
    }
}
