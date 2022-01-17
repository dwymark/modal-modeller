package com.danielwymark.cmmodels.webapp.pages;

import io.javalin.http.Context;


public class BisimulationTesterPage implements Renderable {

    @Override
    public void render(Context ctx) {
        ctx.render("BisimulationTester.jte");
    }
}
