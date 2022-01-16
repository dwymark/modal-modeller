package com.danielwymark.cmmodels.webapp;

import io.javalin.Javalin;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CmmServer {
    private static final Logger logger = LogManager.getLogger(CmmServer.class);
    private static int portNum = 8080;

    public static void main(String[] args) {
        parseArgs(args);
        Javalin javalin = Javalin.create().start(portNum);
        // TODO: Fill in details
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
            //noinspection SwitchStatementWithTooFewBranches
            switch (currentParamName) {
                //noinspection ConstantConditions
                case "port" -> {
                    try {
                        portNum = Integer.parseInt(arg);
                        logger.info("Set port number for CmmServer to " + portNum);
                    }
                    catch (NumberFormatException e) {
                        logger.error("\"" + arg + " could not be parsed as a port number.");
                        System.exit(1);
                    }
                }
            }
        }
    }
}
