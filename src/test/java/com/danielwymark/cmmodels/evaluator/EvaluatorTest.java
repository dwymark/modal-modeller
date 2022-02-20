package com.danielwymark.cmmodels.evaluator;

import com.danielwymark.cmmodels.core.evaluation.NaiveEvaluator;
import com.danielwymark.cmmodels.core.exceptions.OutOfDomainError;
import com.danielwymark.cmmodels.model.ModelTest;
import com.danielwymark.cmmodels.core.syntax.AtomicFormula;
import com.danielwymark.cmmodels.core.syntax.CompoundFormula;
import com.danielwymark.cmmodels.core.syntax.Operator;
import org.junit.Assert;
import org.junit.Test;

public class EvaluatorTest {
    @Test
    public void NaiveEvaluatorCorrectlyEvaluatesFormulas() {
        var model = ModelTest.getMockModel();
        var evaluator = new NaiveEvaluator();

        var w0 = model.getWorld(0);
        var w1 = model.getWorld(1);
        var w2 = model.getWorld(2);
        var w3 = model.getWorld(3);

        var p = new AtomicFormula("p");
        var q = new AtomicFormula("q");

        var pAndQ = new CompoundFormula(Operator.MEET, p, q);
        var pOrQ =  new CompoundFormula(Operator.JOIN, p, q);

        var mustP = new CompoundFormula(Operator.MUST, p);
        var mightP = new CompoundFormula(Operator.MIGHT, p);
        var mightQ = new CompoundFormula(Operator.MIGHT, q);

        var mustBot = new CompoundFormula(Operator.MUST, AtomicFormula.BOTTOM);
        var mightTop = new CompoundFormula(Operator.MIGHT, AtomicFormula.TOP);

        try {
            Assert.assertTrue(evaluator.evaluate(model, w0, p));
            Assert.assertTrue(evaluator.evaluate(model, w0, q));
            Assert.assertFalse(evaluator.evaluate(model, w0, AtomicFormula.BOTTOM));
            Assert.assertTrue(evaluator.evaluate(model, w1, p));
            Assert.assertFalse(evaluator.evaluate(model, w1, q));
            Assert.assertTrue(evaluator.evaluate(model, w3, q));
            Assert.assertFalse(evaluator.evaluate(model, w3, p));

            Assert.assertTrue(evaluator.evaluate(model, w0, pAndQ));
            Assert.assertTrue(evaluator.evaluate(model, w0, pOrQ));
            Assert.assertFalse(evaluator.evaluate(model, w1, pAndQ));
            Assert.assertTrue(evaluator.evaluate(model, w1, pOrQ));
            Assert.assertFalse(evaluator.evaluate(model, w2, pOrQ));
            Assert.assertTrue(evaluator.evaluate(model, w3, pOrQ));

            Assert.assertTrue(evaluator.evaluate(model, w0, mightP));
            Assert.assertTrue(evaluator.evaluate(model, w0, mightQ));
            Assert.assertFalse(evaluator.evaluate(model, w0, mustP));
            Assert.assertFalse(evaluator.evaluate(model, w1, mightP));
            Assert.assertTrue(evaluator.evaluate(model, w3, mightP));

            Assert.assertTrue(evaluator.evaluate(model, w0, mightTop));
            Assert.assertTrue(evaluator.evaluate(model, w1, mightTop));
            Assert.assertFalse(evaluator.evaluate(model, w2, mightTop));
            Assert.assertTrue(evaluator.evaluate(model, w3, mightTop));

            Assert.assertFalse(evaluator.evaluate(model, w0, mustBot));
            Assert.assertFalse(evaluator.evaluate(model, w1, mustBot));
            Assert.assertTrue(evaluator.evaluate(model, w2, mustBot));
            Assert.assertFalse(evaluator.evaluate(model, w3, mustBot));
        }
        catch (OutOfDomainError e) {
            Assert.fail();
        }
    }
}
