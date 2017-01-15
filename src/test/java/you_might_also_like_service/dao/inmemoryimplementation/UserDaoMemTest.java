package you_might_also_like_service.dao.inmemoryimplementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import you_might_also_like_service.dao.UserDao;
import you_might_also_like_service.model.User;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class UserDaoMemTest {
    UserDao userDao;
    String accessToken;
    String userID;
    String cartItemID;
    User testUser;
    HashSet<String> cart = new HashSet<>();

    @Before
    public void setUp() throws Exception {
        userDao = UserDaoMem.getInstance();
        accessToken = "accessToken";
        userID = "123";
        cartItemID = "8";
    }

    @After
    public void tearDown() throws Exception {
        userDao.getAll().clear();
        accessToken = null;
        userID = null;
        cartItemID = null;
        testUser = null;
        cart = null;
    }

    @Test
    public void testSingletonPatternsGetInstance() throws Exception {
        assertEquals(userDao, UserDaoMem.getInstance());
    }

    @Test
    public void testSave() throws Exception {
        userDao.save(accessToken, userID, cartItemID);
        assertEquals(1, userDao.getAll().size());
    }

    @Test
    public void testGetAll() throws Exception {
        assertEquals(0, userDao.getAll().size());
    }

    @Test
    public void testUserIsFound() throws Exception {
        cart = new HashSet<>(Arrays.asList(cartItemID));
        testUser = new User(accessToken, userID, cart);
        userDao.save(accessToken, userID, cartItemID);
        assertEquals(testUser.getUserID(), userDao.find(accessToken, userID).getUserID());
    }

    @Test
    public void testUserIsNotFound() throws Exception {
        assertEquals(null, userDao.find(accessToken, userID));
    }

    @Test
    public void testContainsOneOfTheSpecItemsAtLeast_missingAccessToken() throws Exception {
        userDao.save(accessToken, "1", "1");
        userDao.save(accessToken, "1", "2");
        userDao.save(accessToken, "2", "1");
        userDao.save(accessToken, "2", "2");
        userDao.save(accessToken, "3", "9");
        userDao.save(accessToken, "3", "10");
        userDao.save(accessToken, "4", "5");
        assertEquals(2, userDao.containsOneOfTheSpecItemsAtLeast(accessToken, "1").size());
    }

        @Test
    public void containsOneOfTheSpecItemsAtLeastFails_emptyAccessToken() throws Exception {
        assertEquals(0, userDao.containsOneOfTheSpecItemsAtLeast("", "1").size());
    }

    @Test
    public void containsOneOfTheSpecItemsAtLeastFails_emptyUserId() throws Exception {
        assertEquals(0, userDao.containsOneOfTheSpecItemsAtLeast(accessToken, "").size());
    }

    @Test
    public void selectUniqueItems() throws Exception {
        cart = new HashSet<>(Arrays.asList("1", "2"));
        testUser = new User(accessToken, "1", cart);
        userDao.save(accessToken, "1", "1");
        userDao.save(accessToken, "1", "2");
        userDao.save(accessToken, "2", "1");
        userDao.save(accessToken, "2", "3");
        userDao.save(accessToken, "2", "9");
        userDao.save(accessToken, "3", "10");
        userDao.save(accessToken, "4", "5");
        assertEquals(2, userDao.selectUniqueItems(accessToken, testUser).size());
    }
}