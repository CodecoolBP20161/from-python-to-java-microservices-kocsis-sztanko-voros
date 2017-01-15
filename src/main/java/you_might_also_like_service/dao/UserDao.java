package you_might_also_like_service.dao;

import you_might_also_like_service.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public interface UserDao {
    void save(String accessToken, String userID, String item);
    ArrayList<User> getAll();
    ArrayList containsOneOfTheSpecItemsAtLeast(String accessToken, String userId);
    User find(String accessToken, String userId);
    HashMap<String, Integer> selectUniqueItems(String accessToken, User specUser);
}
