package you_might_also_like_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import you_might_also_like_service.service.YouMightAlsoLikeAPIService;

import static you_might_also_like_service.logging.LogFormatter.FORMAT;

/* This class is responsible for handling data extracted from the URL. */

public class YouMightAlsoLikeAPIController {
    private static final Logger logger = LoggerFactory.getLogger(YouMightAlsoLikeAPIController.class);

    public static final String ACCESS_TOKEN_PARAM_KEY = "accessToken";
    public static final String USER_ID_PARAM_KEY = "userId";
    public static final String CART_ITEM_ID_PARAM_KEY = "cartItemId";

    private final YouMightAlsoLikeAPIService apiService;

    public YouMightAlsoLikeAPIController(YouMightAlsoLikeAPIService apiService) {
        this.apiService = apiService;
    }

    public String status(Request request, Response response) {
        logger.debug(FORMAT.getCustomizedFormatter() + "Status checked.");
        response.status(200);
        return "Server runs.";
    }

    public String saveToDao(Request request, Response response) throws NullPointerException{
        String accessToken = request.queryParams(ACCESS_TOKEN_PARAM_KEY);
        String userId = request.queryParams(USER_ID_PARAM_KEY);
        String cartItemId = request.queryParams(CART_ITEM_ID_PARAM_KEY);
        if (accessToken.isEmpty() || userId.isEmpty() || cartItemId.isEmpty()) {
            logger.error(FORMAT.getCustomizedFormatter() + "One of the parameters extracted from URL (/save) is null during.");
            response.status(400);
            return "Missing parameter in URL, HTTP status error: " + response.status();
        }
        apiService.saveUser(accessToken, userId, cartItemId);
        logger.debug(FORMAT.getCustomizedFormatter() + "Values extracted from URL accepted.");
        response.status(200);
        return "Item successfully saved";
    }

    public String selectByCartItemsFromDao(Request request, Response response) {
        String accessToken = request.queryParams(ACCESS_TOKEN_PARAM_KEY);
        String userId = request.queryParams(USER_ID_PARAM_KEY);
        if (accessToken.isEmpty() || userId.isEmpty()) {
            logger.error(FORMAT.getCustomizedFormatter() + "One of the parameters extracted from URL (/select) is null.");
            response.status(400);
            return "Missing parameter in URL, HTTP status error: " + response.status();
        }
        response.status(200);
        logger.debug(FORMAT.getCustomizedFormatter() + "Values extracted from URL accepted.");
        return apiService.getRecommendations(accessToken, userId).toString();
    }
}
