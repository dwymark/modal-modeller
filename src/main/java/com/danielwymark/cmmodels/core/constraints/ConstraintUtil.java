package com.danielwymark.cmmodels.core.constraints;

import com.danielwymark.cmmodels.core.model.Model;

public class ConstraintUtil {
    private static final TreeConstraint tree = new TreeConstraint();
    private static final AcyclicConstraint acyclic = new AcyclicConstraint();

    public static boolean isTree(Model m) { return tree.holdsOf(m); }
    public static boolean isAcyclic(Model m) { return acyclic.holdsOf(m); }
}
