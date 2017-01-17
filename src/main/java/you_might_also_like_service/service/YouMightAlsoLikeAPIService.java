package you_might_also_like_service.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import you_might_also_like_service.dao.UserDao;
import you_might_also_like_service.dao.inmemoryimplementation.UserDaoMem;
import you_might_also_like_service.model.User;

import java.util.*;

import static you_might_also_like_service.logging.LogFormatter.FORMAT;

/* This class contains mainly the logic of YouMightAlsoLikeAPIService*/

public class YouMightAlsoLikeAPIService {
    private static final Logger logger = LoggerFactory.getLogger(YouMightAlsoLikeAPIService.class);

    private static YouMightAlsoLikeAPIService ourInstance = new YouMightAlsoLikeAPIService();
    private static final int BASE_INDEX = 3;
    private static final int RECOMMENDATION_RANGE = 4;

    public static YouMightAlsoLikeAPIService getInstance() {
        return ourInstance;
    }

    private UserDao userDao;

    private YouMightAlsoLikeAPIService() {
        userDao = UserDaoMem.getInstance();
    }

    public void saveUser(String accessToken, String userID, String item) {
        userDao.save(accessToken, userID, item);
        logger.debug(FORMAT.getCustomizedFormatter() + "User with the following credentials saved: Access Token: {}, User ID: {}, Item: {}", accessToken, userID, item);
        logger.info(FORMAT.getCustomizedFormatter() + "User saved");
    }

    public JSONObject getRecommendations(String accessToken, String userId) {
        logger.info(FORMAT.getCustomizedFormatter() + "Recommendation requested");

        JSONObject json;
        User specUser = userDao.find(accessToken, userId);
        logger.debug(FORMAT.getCustomizedFormatter() + "SpecUser found: {}", userDao.find(accessToken, userId));
        ArrayList<User> allUsers = userDao.containsOneOfTheSpecItemsAtLeast(accessToken, userId);
        if (allUsers.size() == 0) {
            json = new JSONObject().put("recommendations", allUsers);
            return json;
        }
        logger.debug(FORMAT.getCustomizedFormatter() + "Users with carts containing specUser's items at leans once in their own pocket: {}", allUsers);
        HashMap<String, Integer> uniqueItems = userDao.selectUniqueItems(specUser.getAccessToken(), specUser);
        logger.debug(FORMAT.getCustomizedFormatter() + "Unique items selected in HashMap: {}", uniqueItems);

        logger.info(FORMAT.getCustomizedFormatter() + "Calculation of recommendations just started.");
        for (User user : allUsers) {
            Integer power = -1;
            for (String item : user.getItems()) {
                if (specUser.getItems().contains(item)) {
                    power++;
                }
            }
            for (String item : user.getItems()) {
                if (!specUser.getItems().contains(item)) {
                    uniqueItems.put(item, (int) (uniqueItems.get(item) + Math.pow(BASE_INDEX, power)));
                }
            }
        }

        Map<String, Integer> resultHM = new LinkedHashMap<>();
        logger.debug(FORMAT.getCustomizedFormatter() + "Unsorted resultSet: {}", resultHM);

        uniqueItems.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEachOrdered(x -> resultHM.put(x.getKey(), x.getValue()));
        logger.debug(FORMAT.getCustomizedFormatter() + "Sorted resultSet returned: {}", resultHM);

        Set resultS = resultHM.keySet();
        Integer range = (resultS.size() < RECOMMENDATION_RANGE) ? resultS.size() : RECOMMENDATION_RANGE;
        json = new JSONObject().put("recommendations", new ArrayList<>(resultS).subList(0, range));
        logger.debug(FORMAT.getCustomizedFormatter() + "Following JSON returned: {}", json);
        logger.info(FORMAT.getCustomizedFormatter() + "Recommendation returned.");
        return json;
    }
}
