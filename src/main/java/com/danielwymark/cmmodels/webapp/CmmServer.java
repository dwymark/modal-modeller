package com.danielwymark.cmmodels.webapp;

import com.danielwymark.cmmodels.webapp.pages.BisimulationTesterPage;
import com.danielwymark.cmmodels.webapp.pages.CreateModelPage;
import com.danielwymark.cmmodels.webapp.pages.ViewModelPage;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;

public class CmmServer {
    private static final Logger logger = LogManager.getLogger(CmmServer.class);
    private static int portNum = 8080;
    private static String imagesDirectory = System.getProperty("user.dir");

    public static void main(String[] args) {
        parseArgs(args);
        Javalin app = Javalin.create(config -> config.addStaticFiles(staticFiles -> {
            staticFiles.hostedPath = "/images";
            staticFiles.directory = imagesDirectory;
            staticFiles.location = Location.EXTERNAL;
        })).start(portNum);

        app.get("/view-model/{modelNum}", ctx -> {
            var page = new ViewModelPage(ctx.pathParam("modelNum"), imagesDirectory);
            page.render(ctx);
        });
        app.get("/create-model/{modelNum}", ctx -> {
            var page = new CreateModelPage(ctx.pathParam("modelNum"));
            page.render(ctx);
        });
        app.get("/create-model/{modelNum}/add{numWorldsToAdd}", ctx -> {
            int numWorldsToAdd = Integer.parseInt(ctx.pathParam("numWorldsToAdd"));
            var page = new CreateModelPage(ctx.pathParam("modelNum"), numWorldsToAdd);
            page.render(ctx);
        });
//        app.get("/create-model/{modelNum}/{source}->{target}", ctx -> {
//            int numWorldsToAdd = Integer.parseInt(ctx.pathParam("numWorldsToAdd"));
//            var page = new CreateModelPage(ctx.pathParam("modelNum"), numWorldsToAdd);
//            page.render(ctx);
//        });
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
                    }
                    catch (NumberFormatException e) {
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
            }
            requiredArguments--;
        }
    }
}
