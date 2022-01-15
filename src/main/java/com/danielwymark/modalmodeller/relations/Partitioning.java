package com.danielwymark.modalmodeller.relations;

import com.danielwymark.modalmodeller.model.World;

import java.util.List;
import java.util.Set;

public record Partitioning(Set<World> universe, List<Set<World>> blocks) {
}
