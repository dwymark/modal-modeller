package com.danielwymark.cmmodels.core.constraints;

import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.World;

@SuppressWarnings("unused")
public class ConstraintUtil {
    private static final TreeConstraint tree = new TreeConstraint();
    private static final AcyclicConstraint acyclic = new AcyclicConstraint();
    private static final UnreachableConstraint unreachable = new UnreachableConstraint();


    public static boolean isAcyclic(Model m) { return acyclic.holdsOf(m); }
    public static boolean isTree(Model m, World w) { return tree.holdsOf(m, w); }
    public static boolean isTree(Model m, int w) { return tree.holdsOf(m, w); }
    public static boolean isUnreachable(Model m, World w) { return unreachable.holdsOf(m, w); }
    public static boolean isUnreachable(Model m, int w) { return unreachable.holdsOf(m, w); }
}
