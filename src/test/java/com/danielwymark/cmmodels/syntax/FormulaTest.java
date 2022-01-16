package com.danielwymark.cmmodels.syntax;

import com.danielwymark.cmmodels.core.syntax.AtomicFormula;
import com.danielwymark.cmmodels.core.syntax.Formula;
import org.junit.Assert;
import org.junit.Test;

public class FormulaTest {
    @Test
    public void AtomicFormulaStringsAreCorrect() {
        var atomP = new AtomicFormula("p");
        var atomQ = new AtomicFormula("q");
        Assert.assertEquals(atomP.toString(), "p");
        Assert.assertEquals(atomQ.toString(), "q");
    }

    @Test
    public void OperatorFormulaStringsAreCorrect() {
        var p = new AtomicFormula("p");
        var q = new AtomicFormula("q");

        Assert.assertEquals("¬p", Formula.negate(p).toString());
        Assert.assertEquals("□q", Formula.must(q).toString());
        Assert.assertEquals("◇q", Formula.might(q).toString());

        Assert.assertEquals("(¬p⋁q)", Formula.negate(p).or(q).toString());
        Assert.assertEquals("(□q⋀◇p)", Formula.must(q).and(Formula.might(p)).toString());

        Assert.assertEquals("((¬p⋁q)⋀(¬q⋁p))", p.implies(q).and(q.implies(p)).toString());
    }
}
