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
        var p = new AtomicFormula("p");
        var q = new AtomicFormula("q");

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
}
