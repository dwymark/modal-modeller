package com.danielwymark.cmmodels.webapp.pages;

import io.javalin.http.Context;

public interface Renderable {
    void render(Context ctx);
}
