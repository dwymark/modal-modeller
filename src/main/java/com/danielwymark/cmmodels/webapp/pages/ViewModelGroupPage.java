package com.danielwymark.cmmodels.webapp.pages;

import com.danielwymark.cmmodels.core.exceptions.InvalidPageConfigurationError;
import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.PointedModel;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record ViewModelGroupPage(List<Model> models) implements Renderable {
    public ViewModelGroupPage {
        if (models == null || models.size() < 1) {
            throw new InvalidPageConfigurationError();
        }
    }

    @Override
    public void render(Context ctx) {
        String representative = models.get(0).modelNumber();
        List<String> allModels = models.stream().map(Model::modelNumber).toList();
        ctx.render("ViewModelGroup.jte", Map.of(
                "representativeModelNum", representative,
                "allModelNums", allModels
        ));
    }
}
