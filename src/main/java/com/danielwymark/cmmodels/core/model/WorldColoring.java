package com.danielwymark.cmmodels.core.model;

import guru.nidi.graphviz.model.Node;

public interface WorldColoring {
    Node color(World world, Node node);
}
