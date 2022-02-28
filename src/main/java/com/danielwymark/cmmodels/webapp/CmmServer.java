package com.danielwymark.cmmodels.webapp;

import com.danielwymark.cmmodels.core.model.Model;
import com.danielwymark.cmmodels.core.model.ModelBuilder;
import com.danielwymark.cmmodels.webapp.pages.*;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.template.JavalinJte;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CmmServer {
    private static final Logger logger = LogManager.getLogger(CmmServer.class);
    private static int portNum = 8080;
    private static String imagesDirectory = System.getProperty("user.dir");
    private static boolean usePrecompiledTemplates;
    private static String dotExecutablePath;

    public static void main(String[] args) {
        parseArgs(args);

        if (dotExecutablePath != null) {
            logger.debug("Attempting to use dot executable at path " + dotExecutablePath);
            Graphviz.useEngine(new GraphvizCmdLineEngine(dotExecutablePath));
        }

        if (usePrecompiledTemplates) {
            TemplateEngine templateEngine = TemplateEngine.createPrecompiled(ContentType.Html);
            JavalinJte.configure(templateEngine);
        }

        Javalin app = Javalin.create(config -> config.addStaticFiles(staticFiles -> {
            staticFiles.hostedPath = "/images";
            staticFiles.directory = imagesDirectory;
            staticFiles.location = Location.EXTERNAL;
        })).start(portNum);

        // ViewModelPage
        //--------------------------------------------------------------------------------------------------------------
        app.get("/view-model/{modelNum}", ctx -> {
            var page = new ViewModelPage(ctx.pathParam("modelNum"), imagesDirectory);
            page.render(ctx);
        });

        // ViewBisimulationPage
        //--------------------------------------------------------------------------------------------------------------
        app.get("/view-bisimulation/{leftModelNum}/{rightModelNum}", ctx -> {
            String leftModelNum = ctx.pathParam("leftModelNum");
            String rightModelNum = ctx.pathParam("rightModelNum");
            var page = new ViewBisimulationPage(leftModelNum, rightModelNum, imagesDirectory);
            page.render(ctx);
        });

        app.post("/view-bisimulation", ctx -> {
            String leftModelNum = ctx.formParam("leftModelNum");
            String rightModelNum = ctx.formParam("rightModelNum");
            var page = new ViewBisimulationPage(leftModelNum, rightModelNum, imagesDirectory);
            page.render(ctx);
        });

        // CreateModelPage
        //--------------------------------------------------------------------------------------------------------------
        app.get("/create-model/{modelNum}", ctx -> {
            var page = new CreateModelPage(ctx.pathParam("modelNum"));
            page.render(ctx);
        });
        app.post("/create-model", ctx -> {
            var page = new CreateModelPage(ctx.formParam("modelNum"));
            page.render(ctx);
        });
        app.get("/create-model/{modelNum}/add/{numWorldsToAdd}", ctx -> {
            int numWorldsToAdd;
            try {
                numWorldsToAdd = Integer.parseInt(ctx.pathParam("numWorldsToAdd"));
            } catch (NumberFormatException e) {
                ctx.status(400);
                return;
            }

            var page = new CreateModelPage(ctx.pathParam("modelNum"), numWorldsToAdd);
            page.render(ctx);
        });
        app.post("/create-model/{modelNum}/toggle", ctx -> {
            String sourceParam = ctx.formParam("sourceWorld");
            String targetParam = ctx.formParam("targetWorld");
            if (sourceParam == null || targetParam == null) {
                ctx.status(400);
                return;
            }

            int sourceWorld, targetWorld;
            try {
                sourceWorld = Integer.parseInt(sourceParam);
                targetWorld = Integer.parseInt(targetParam);
            } catch (NumberFormatException e) {
                ctx.status(400);
                return;
            }

            var page = new CreateModelPage(ctx.pathParam("modelNum"), Map.of(sourceWorld, targetWorld));
            page.render(ctx);
        });

        // BisimulationTesterPage
        //--------------------------------------------------------------------------------------------------------------
        app.get("/bisimulation-tester", ctx -> ctx.render("BisimulationTester.jte"));

        // ModelMinimizationPage
        //--------------------------------------------------------------------------------------------------------------
        app.post("/view-minimized", ctx -> {
            var originalModelNumber = ctx.formParam("modelNum");

            var minimizedModelNumber = ModelBuilder.buildFromModelNumber(originalModelNumber)
                    .minimize().modelNumber();

            var page = new ViewModelPage(minimizedModelNumber, imagesDirectory);
            page.render(ctx);
        });
        app.get("/model-minimizer", ctx -> ctx.render("ModelMinimizer.jte"));

        // Bisimulation Classes
        //--------------------------------------------------------------------------------------------------------------
        app.get("/view-model-group/{modelNums}", ctx -> {
            List<Model> models = Arrays.stream(ctx.pathParam("modelNums").split("_"))
                    .map(ModelBuilder::buildFromModelNumber)
                    .toList();
            var page = new ViewModelGroupPage(models);
            page.render(ctx);
        });
        app.get("/view-model-group-list/{modelNumsList}", ctx -> {
            List<List<String>> modelNumsList = Arrays.stream(ctx.pathParam("modelNumsList").split("-"))
                    .map(list -> Arrays.stream(list.split("_")).toList())
                    .toList();
            ctx.render("ViewModelGroupList.jte", Map.of("modelNumsList", modelNumsList));
        });
    }

    private static void parseArgs(String[] args) {
        String currentParamName = null;
        int requiredArguments = 0;
        for (var arg : args) {
            boolean isParamName = arg.charAt(0) == '-';
            if (isParamName && requiredArguments != 0) {
                logger.error("Missing " + requiredArguments + " required argument(s) for parameter: \"" + currentParamName + "\"");
                System.exit(1);
            }

            if (isParamName) {
                switch (arg) {
                    case "-p", "--port" -> {
                        currentParamName = "port";
                        requiredArguments = 1;
                    }
                    case "-i", "--images-directory" -> {
                        currentParamName = "images-directory";
                        requiredArguments = 1;
                    }
                    case "-jte", "--use-precompiled-jte" -> {
                        currentParamName = "use-precompiled-jte";
                        //noinspection ConstantConditions
                        requiredArguments = 0;
                        usePrecompiledTemplates = true;
                    }
                    case "-d", "--dot-executable" -> {
                        currentParamName = "dot-executable";
                        requiredArguments = 1;
                    }
                    default -> {
                        logger.error("Unrecognized parameter name: \"" + arg + "\"");
                        System.exit(1);
                    }
                }
                continue;
            }

            assert (!isParamName);
            if (currentParamName == null) {
                logger.error("Parsing error. You may have provided too many arguments for a parameter, or perhaps " +
                        "you forgot to list a parameter name before your first argument.");
                System.exit(1);
            }
            switch (currentParamName) {
                case "port" -> {
                    try {
                        portNum = Integer.parseInt(arg);
                        logger.info("Set port number for CmmServer to " + portNum);
                    } catch (NumberFormatException e) {
                        logger.error("\"" + arg + "\" could not be parsed as a port number.");
                        System.exit(1);
                    }
                }
                case "images-directory" -> {
                    if (!new File(arg).isDirectory()) {
                        logger.error("\"" + arg + "\" is not a valid directory.");
                        System.exit(1);
                    }
                    imagesDirectory = arg;
                }
                case "dot-executable" -> {
                    Path dot = Path.of(arg);
                    if (!(Files.isRegularFile(dot) && Files.isExecutable(dot))) {
                        logger.error("\"" + arg + "\" is not an executable file.");
                        System.exit(1);
                    }
                    dotExecutablePath = arg;
                }
            }
            requiredArguments--;
        }
    }
}
