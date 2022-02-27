package com.danielwymark.cmmodels.core.model;

import com.danielwymark.cmmodels.core.exceptions.InvalidModelNumberError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.text.html.Option;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Convenience class to ease the process of building a model.
 * The factory is mutable, but any given model is immutable once
 * it has been produced.
 */
public class ModelBuilder {
    private final Logger logger = LogManager.getLogger(ModelBuilder.class);

    private int numWorlds;
    private final HashMap<Integer, Set<Integer>> tentativeAccessMap;
    private final HashMap<Integer, Set<String>> tentativeValuationMap;
    private Optional<Integer> pointedWorld = Optional.empty();

    public ModelBuilder(String modelNum) {
        tentativeAccessMap = new HashMap<>();
        tentativeValuationMap = new HashMap<>();

        Pattern r = Pattern.compile("[(]?(\\d+)w(\\d+)(,([0-9]+))?[)]?");
        Matcher m = r.matcher(modelNum);
        if (!m.matches())
            throw new InvalidModelNumberError();

        numWorlds = Integer.parseInt(m.group(1));
        var accessibilityRelation = new BigInteger(m.group(2));

        // Every world W's accessibility relation given by a segment of a bitstream.
        // Each bit in the segment indicates whether W is related to a particular world.

        var mask = BigInteger.ONE;
        for (int i = 0; i < numWorlds; ++i) {
            for (int j = 0; j < numWorlds; ++j) {
                var masked = mask.and(accessibilityRelation);
                if (masked.compareTo(BigInteger.ZERO) != 0) {
                    addRelation(i, j);
                }
                mask = mask.shiftLeft(1);
            }
        }

        String pointedWorldStr = m.group(4);
        if (pointedWorldStr != null) {
            pointedWorld = Optional.of(Integer.parseInt(pointedWorldStr));
            if (pointedWorld.get() >= numWorlds)
                numWorlds = pointedWorld.get() + 1;
        }
    }

    public ModelBuilder(int numWorlds) {
        this.numWorlds = numWorlds;
        tentativeAccessMap = new HashMap<>();
        tentativeValuationMap = new HashMap<>();
    }

    public ModelBuilder() {
        this(0);
    }

    public static Model buildFromModelNumber(String modelNum) {
        var modelBuilder = new ModelBuilder(modelNum);
        return modelBuilder.build();
    }

    public int getNumWorlds() {
        return numWorlds;
    }

    public void setNumWorlds(int numWorlds) {
        this.numWorlds = numWorlds;
        for (var world : tentativeAccessMap.keySet()) {
            if (world >= numWorlds) {
                tentativeAccessMap.remove(world);
            }
            else {
                tentativeAccessMap.put(world,
                        new HashSet<>(tentativeAccessMap.get(world).stream()
                                .filter(x-> x < numWorlds)
                                .collect(Collectors.toSet())));
            }
        }
        for (var world : tentativeValuationMap.keySet()) {
            if (world >= numWorlds) {
                tentativeValuationMap.remove(world);
            }
        }
    }

    public void addRelation(int w1, int w2) {
        if (w1 >= numWorlds || w2 >= numWorlds) {
            numWorlds = Math.max(w1, w2) + 1;
            logger.debug("Increasing number of worlds to " + numWorlds + " to account for relation (" + w1 + "," + w2 + ")");
        }

        if (tentativeAccessMap.containsKey(w1)) {
            tentativeAccessMap.get(w1).add(w2);
        } else {
            tentativeAccessMap.put(w1, new HashSet<>(Set.of(w2)));
        }
    }

    public void toggleRelation(int w1, int w2) {
        if (!tentativeAccessMap.containsKey(w1)) {
            tentativeAccessMap.put(w1, new HashSet<>(Set.of(w2)));
            return;
        }
        if (tentativeAccessMap.get(w1).contains(w2)) {
            tentativeAccessMap.get(w1).remove(w2);
        }
        else {
            addRelation(w1, w2);
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

        var model = new Model(numWorlds, accessMap, valuationMap);
        if (pointedWorld.isPresent()) {
            return new PointedModel(model, model.getWorld(pointedWorld.get()));
        }
        return model;
    }
}
