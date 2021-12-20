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
        var p = new SingularFormula("p");
        var q = new SingularFormula("q");

        Assert.assertEquals("¬p", Formula.negate(p).toString());
        Assert.assertEquals("□q", Formula.must(q).toString());
        Assert.assertEquals("◇q", Formula.might(q).toString());

        Assert.assertEquals("(¬p⋁q)", Formula.negate(p).or(q).toString());
        Assert.assertEquals("(□q⋀◇p)", Formula.must(q).and(Formula.might(p)).toString());

        Assert.assertEquals("((¬p⋁q)⋀(¬q⋁p))", p.implies(q).and(q.implies(p)).toString());
    }
}
