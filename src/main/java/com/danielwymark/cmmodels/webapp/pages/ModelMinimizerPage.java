package com.danielwymark.cmmodels.webapp.pages;

import io.javalin.http.Context;


public class ModelMinimizerPage implements Renderable {

    @Override
    public void render(Context ctx) {
        ctx.render("ModelMinimizer.jte");
    }
}
