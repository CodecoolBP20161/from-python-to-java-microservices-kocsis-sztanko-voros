package you_might_also_like_service.dao.inmemoryimplementation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import you_might_also_like_service.dao.UserDao;

import static org.junit.Assert.*;

public class UserDaoMemTest {
    UserDao userDao;
    String accessToken;
    String userID;
    String cartItemID;

    @Before
    public void setUp() throws Exception {
        userDao = UserDaoMem.getInstance();
        accessToken = "accessToken";
        userID = "123";
        cartItemID = "8";
    }

    @After
    public void tearDown() throws Exception {
        userDao = null;
        accessToken = null;
        userID = null;
        cartItemID = null;

    }

    @Test
    public void testSingletonPatternsGetInstance() throws Exception {
        assertEquals(userDao, UserDaoMem.getInstance());
    }

    @Test
    public void save() throws Exception {
        userDao.save(accessToken, userID, cartItemID);
        assertEquals(1, userDao.getAll().size());
    }

    @Test
    public void getDATA() throws Exception {

    }

    @Test
    public void find() throws Exception {

    }

    @Test
    public void containsOneOfTheSpecItemsAtLeast() throws Exception {

    }

    @Test
    public void selectUniqueItems() throws Exception {

    }

}