package com.danielwymark.cmmodels.core.constraints;

import com.danielwymark.cmmodels.core.model.Model;

public class TreeConstraint implements Constraint {
    @Override
    public boolean holdsOf(Model m) {
        return false;
    }
}