package com.danielwymark.cmmodels.constraints;

import com.danielwymark.cmmodels.core.constraints.ConstraintUtil;
import com.danielwymark.cmmodels.core.model.ModelBuilder;
import com.danielwymark.cmmodels.core.syntax.AtomicFormula;
import com.danielwymark.cmmodels.core.syntax.Formula;
import org.junit.Assert;
import org.junit.Test;

public class ConstraintTest {
    @Test
    public void AcyclicConstraintIsAccurate() {
        var modelBuilder = new ModelBuilder();
        modelBuilder.addRelation(0, 1);
        Assert.assertTrue(ConstraintUtil.isAcyclic(modelBuilder.build()));

        modelBuilder.addRelation(1, 0);
        Assert.assertFalse(ConstraintUtil.isAcyclic(modelBuilder.build()));

        modelBuilder = new ModelBuilder();
        modelBuilder.addRelation(0, 0);
        Assert.assertFalse(ConstraintUtil.isAcyclic(modelBuilder.build()));

        modelBuilder.setNumWorlds(3);
        Assert.assertFalse(ConstraintUtil.isAcyclic(modelBuilder.build()));

        modelBuilder = new ModelBuilder(3);
        Assert.assertTrue(ConstraintUtil.isAcyclic(modelBuilder.build()));

        modelBuilder.addRelation(0,1);
        modelBuilder.addRelation(1,2);
        Assert.assertTrue(ConstraintUtil.isAcyclic(modelBuilder.build()));

        modelBuilder.addRelation(2, 0);
        Assert.assertFalse(ConstraintUtil.isAcyclic(modelBuilder.build()));
    }

    @Test
    public void UnreachableTestIsAccurate() {
        var modelBuilder = new ModelBuilder();
        modelBuilder.addRelation(0, 1);
        var model = modelBuilder.build();
        Assert.assertTrue(ConstraintUtil.isUnreachable(model, 0));
        Assert.assertFalse(ConstraintUtil.isUnreachable(model, 1));

        modelBuilder.addRelation(1, 0);
        model = modelBuilder.build();
        Assert.assertFalse(ConstraintUtil.isUnreachable(model, 0));
        Assert.assertFalse(ConstraintUtil.isUnreachable(model, 1));

        modelBuilder.setNumWorlds(3);
        model = modelBuilder.build();
        Assert.assertTrue(ConstraintUtil.isUnreachable(model, 2));
    }

    @Test
    public void TreeTestIsAccurate() {
        var modelBuilder = new ModelBuilder(1);
        var model = modelBuilder.build();
        Assert.assertTrue(ConstraintUtil.isTree(model, 0));

        modelBuilder.addRelation(0, 1);
        model = modelBuilder.build();
        Assert.assertTrue(ConstraintUtil.isTree(model, 0));
        Assert.assertFalse(ConstraintUtil.isTree(model, 1));

        modelBuilder.addRelation(0, 2);
        modelBuilder.addRelation(2, 3);
        model = modelBuilder.build();
        Assert.assertTrue(ConstraintUtil.isTree(model, 0));

        modelBuilder.addRelation(1, 3);
        model = modelBuilder.build();
        Assert.assertTrue(ConstraintUtil.isAcyclic(model));
        Assert.assertFalse(ConstraintUtil.isTree(model, 0));

        modelBuilder = new ModelBuilder(2);
        model = modelBuilder.build();
        Assert.assertFalse(ConstraintUtil.isTree(model, 0));
    }
}
