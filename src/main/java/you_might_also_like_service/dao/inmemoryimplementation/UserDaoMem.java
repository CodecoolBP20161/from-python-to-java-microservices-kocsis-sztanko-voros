package you_might_also_like_service.dao.inmemoryimplementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import you_might_also_like_service.dao.UserDao;
import you_might_also_like_service.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static you_might_also_like_service.logging.LogFormatter.FORMAT;

/* Implementation which stores in computer's memory */

public class UserDaoMem implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoMem.class);

    private static UserDaoMem ourInstance = new UserDaoMem();

    public static UserDaoMem getInstance() {
        return ourInstance;
    }

    private ArrayList<User> DATA;

    private UserDaoMem() {
        DATA = new ArrayList<User>();
    }

    public ArrayList<User> getAll() {
        return DATA;
    }

    // finds specUser - person, whose cart is the benchmark as opposed to the others' - in storage
    @Override
    public User find(String accessToken, String userId) {
        logger.info(FORMAT.getCustomizedFormatter() + "Looking for a certain User in the storage.");
        logger.debug(FORMAT.getCustomizedFormatter() + "Credentials: Access Token: {}, User ID: {}", accessToken, userId);

        User user = null;
        for (User element : DATA) {
            if (element.getAccessToken().equals(accessToken) && element.getUserID().equals(userId)) {
                user = element;
                logger.info(FORMAT.getCustomizedFormatter() + "Following user found: {}", userId);
            }
        }
        return user;
    }

    // saves user into storage
    @Override
    public void save(String accessToken, String userID, String item) {
        logger.info(FORMAT.getCustomizedFormatter() + "Saving process just started.");

        User user = find(accessToken, userID);
        if (user == null) {
            logger.info(FORMAT.getCustomizedFormatter() + "New User added to storage.");
            user = new User(accessToken, userID, new HashSet<>(Arrays.asList(item)));
            DATA.add(user);
        } else {
            logger.info(FORMAT.getCustomizedFormatter() + "User already exists in storage. Updating cart with new item is done.");
            user.getItems().add(item);
        }
    }

    // selects all the users whose cart items contains one of the specUser's cartItem
    @Override
    public ArrayList<User> containsOneOfTheSpecItemsAtLeast(String accessToken, String userId) {
        logger.info(FORMAT.getCustomizedFormatter() + "Collecting all users, whose cart contains one of the specUser's items just started.");

        ArrayList users = new ArrayList<>();
        try {
            HashSet<String> items = find(accessToken, userId).getItems();
            for (User user : DATA) {
                for (String item : items) {
                    if (user.getItems().contains(item)) {
                        users.add(user);
                        break;
                    }
                }
            }
        } catch (NullPointerException e) {
            logger.warn(FORMAT.getCustomizedFormatter() + "No access token and user ID matched during searching in storage.");
            return users;
        }
        logger.debug(FORMAT.getCustomizedFormatter() + "All users selected: {}", users);
        return users;
    }

    // selects all items that are unique among users selected by containsOneOfTheSpecItemsAtLeast() method
    @Override
    public HashMap<String, Integer> selectUniqueItems(String accessToken, User specUser) {
        logger.info(FORMAT.getCustomizedFormatter() + "Selecting unique items just started.");

        HashSet<String> uniqueItemsSet = new HashSet<>();
        for (User user : containsOneOfTheSpecItemsAtLeast(accessToken, specUser.getUserID())) {
            if (user.getAccessToken().equals(accessToken)) {
                uniqueItemsSet.addAll(user.getItems());
            }
        }

        HashMap<String, Integer> uniqueItemsMap = new HashMap<>();
        for (String id : uniqueItemsSet) {
            if (!specUser.getItems().contains(id)) {
                uniqueItemsMap.put(id, 0);
            }
        }
        logger.debug(FORMAT.getCustomizedFormatter() + "Unique items and their importance: {}", uniqueItemsMap);
        return uniqueItemsMap;
    }
}
