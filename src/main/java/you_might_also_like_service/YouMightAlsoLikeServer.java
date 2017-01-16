package you_might_also_like_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import you_might_also_like_service.controller.YouMightAlsoLikeAPIController;
import you_might_also_like_service.service.YouMightAlsoLikeAPIService;

import java.net.URISyntaxException;

import static spark.Spark.*;
import static you_might_also_like_service.logging.LogFormatter.FORMAT;

/* Responsible for running the server of the MicroService */

public class YouMightAlsoLikeServer {
    private static final Logger logger = LoggerFactory.getLogger(YouMightAlsoLikeServer.class);

    private YouMightAlsoLikeAPIController controller;

    public static void main(String[] args) {
        logger.debug(FORMAT.getCustomizedFormatter() + "Server started to run: {}", YouMightAlsoLikeServer.class.getName());

        YouMightAlsoLikeServer application = new YouMightAlsoLikeServer();

        application.controller = new YouMightAlsoLikeAPIController(YouMightAlsoLikeAPIService.getInstance());

        port(60002);

        // --- MAPPINGS ---
        get("/api/preferences/status", application.controller::status);
        get("/api/preferences/save", application.controller::saveToDao);
        get("/api/preferences/select", application.controller::selectByCartItemsFromDao);

        // --- EXCEPTION HANDLING ---
        exception(URISyntaxException.class, (exception, request, response) -> {
            response.status(500);
            response.body(String.format("URI building error, maybe wrong format? : %s", exception.getMessage()));
        });

        exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.body(String.format("Unexpected error occurred: %s", exception.getMessage()));
        });
    }
}