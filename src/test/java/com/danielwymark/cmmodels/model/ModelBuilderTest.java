package com.danielwymark.cmmodels.model;

import com.danielwymark.cmmodels.core.exceptions.InvalidModelNumberError;
import com.danielwymark.cmmodels.core.exceptions.OutOfDomainError;
import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.ModelBuilder;
import com.danielwymark.cmmodels.core.model.PointedModel;
import com.danielwymark.cmmodels.core.model.World;
import com.danielwymark.cmmodels.core.syntax.AtomicFormula;
import org.junit.Assert;
import org.junit.Test;

public class ModelBuilderTest {

    @Test
    public void InvalidModelNumberRaisesException() {
        Assert.assertThrows(InvalidModelNumberError.class, ()->ModelBuilder.buildFromModelNumber("1234"));
        Assert.assertThrows(InvalidModelNumberError.class, ()->ModelBuilder.buildFromModelNumber("Test1w234"));
    }

    @Test
    public void ModelNumberBuiltCorrectly() {
        var model = ModelBuilder.buildFromModelNumber("1w0");
        Assert.assertEquals(model.getNumWorlds(), 1);
        Assert.assertTrue(model.worldsAccessibleFrom(0).isEmpty());

        model = ModelBuilder.buildFromModelNumber("1w1");
        Assert.assertEquals(model.getNumWorlds(), 1);
        Assert.assertTrue(model.worldsAccessibleFrom(0).contains(model.getWorld(0)));

        model = ModelBuilder.buildFromModelNumber("3w150"); // 010 010 110
        Assert.assertEquals(model.getNumWorlds(), 3);
        Assert.assertFalse(model.accessible(0,0));
        Assert.assertTrue (model.accessible(0,1));
        Assert.assertTrue (model.accessible(0,2));
        Assert.assertFalse(model.accessible(1,0));
        Assert.assertTrue (model.accessible(1,1));
        Assert.assertFalse(model.accessible(1,2));
        Assert.assertFalse(model.accessible(2,0));
        Assert.assertTrue (model.accessible(2,1));
        Assert.assertFalse(model.accessible(2,2));

        model = ModelBuilder.buildFromModelNumber("(3w150,2)"); // 010 010 110
        Assert.assertTrue(model instanceof PointedModel);
        Assert.assertEquals(model.getNumWorlds(), 3);
        Assert.assertFalse(model.accessible(0,0));
        Assert.assertTrue (model.accessible(0,1));
        Assert.assertTrue (model.accessible(0,2));
        Assert.assertFalse(model.accessible(1,0));
        Assert.assertTrue (model.accessible(1,1));
        Assert.assertFalse(model.accessible(1,2));
        Assert.assertFalse(model.accessible(2,0));
        Assert.assertTrue (model.accessible(2,1));
        Assert.assertFalse(model.accessible(2,2));
        Assert.assertEquals(((PointedModel)model).getPointedWorld().index(), 2);
    }

}
