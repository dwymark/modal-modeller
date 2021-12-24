package com.danielwymark.modalmodeller.relations;

import com.danielwymark.modalmodeller.model.Model;
import com.danielwymark.modalmodeller.model.World;

import java.util.HashMap;

public class Relation {
    public final Model left;
    public final Model right;
    private final HashMap<World, World> map;

    public Relation(Model left, Model right, HashMap<World, World> map) {
        this.left = left;
        this.right = right;
        this.map = map;
    }
}
