package com.danielwymark.modalmodeller.syntax;

import org.junit.Assert;
import org.junit.Test;

public class FormulaTest {
    @Test
    public void AtomicFormulaStringsAreCorrect() {
        var atomP = new SingularFormula("p");
        var atomQ = new SingularFormula("q");
        Assert.assertEquals(atomP.toString(), "p");
        Assert.assertEquals(atomQ.toString(), "q");
    }

    @Test
    public void OperatorFormulaStringsAreCorrect() {
        var atomP = new SingularFormula("p");
        var atomQ = new SingularFormula("q");
        var atomR = new SingularFormula("q");

        var notP = new CompoundFormula(Operator.NEGATE, atomP);
        var mustQ = new CompoundFormula(Operator.MUST, atomQ);
        var mightP = new CompoundFormula(Operator.MIGHT, atomR);

        Assert.assertEquals("¬p", notP.toString());
        Assert.assertEquals("□q", mustQ.toString());
        Assert.assertEquals("◇q", mightP.toString());

        Assert.assertEquals("(¬p⋁q)", new CompoundFormula(Operator.JOIN, notP, atomQ).toString());
        Assert.assertEquals("(□q⋀□q)", new CompoundFormula(Operator.MEET, mustQ, mustQ).toString());
    }
}
