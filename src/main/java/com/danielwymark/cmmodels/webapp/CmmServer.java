package com.danielwymark.cmmodels.webapp;

import com.danielwymark.cmmodels.webapp.pages.BisimulationTesterPage;
import com.danielwymark.cmmodels.webapp.pages.CreateModelPage;
import com.danielwymark.cmmodels.webapp.pages.ViewBisimulationPage;
import com.danielwymark.cmmodels.webapp.pages.ViewModelPage;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.rendering.template.JavalinJte;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;

public class CmmServer {
    private static final Logger logger = LogManager.getLogger(CmmServer.class);
    private static int portNum = 8080;
    private static String imagesDirectory = System.getProperty("user.dir");
    private static String precompiledTemplatesPath;

    public static void main(String[] args) {
        parseArgs(args);

        if (precompiledTemplatesPath != null) {
            Path targetDirectory = Path.of(precompiledTemplatesPath);
            TemplateEngine templateEngine = TemplateEngine.createPrecompiled(targetDirectory, ContentType.Html);
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
        app.get("/bisimulation-tester", ctx -> {
            var page = new BisimulationTesterPage();
            page.render(ctx);
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
                    case "-d", "--images-directory" -> {
                        currentParamName = "images-directory";
                        requiredArguments = 1;
                    }
                    case "-jte", "--precompiled-jte-directory" -> {
                        currentParamName = "precompiled-jte-directory";
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
                case "precompiled-jte-directory" -> {
                    if (!new File(arg).isDirectory()) {
                        logger.error("\"" + arg + "\" is not a valid directory.");
                        System.exit(1);
                    }
                    precompiledTemplatesPath = arg;
                }
            }
            requiredArguments--;
        }
    }
}
