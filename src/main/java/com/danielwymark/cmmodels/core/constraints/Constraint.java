package com.danielwymark.cmmodels.core.constraints;

import com.danielwymark.cmmodels.core.model.Model;

public interface Constraint {
    boolean holdsOf(Model m);
}
