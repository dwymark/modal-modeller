package com.danielwymark.cmmodels.core.constraints;

import com.danielwymark.cmmodels.core.model.Model;

public interface ModelConstraint {
    boolean holdsOf(Model m);
}
