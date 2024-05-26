package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.UserDao;
import dataaccess.dao.memoryDao.MemoryAuthDao;
import dataaccess.dao.memoryDao.MemoryUserDao;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;




public class UserServiceTests {

    private static UserService userService;
    private static final UserData testUser = new UserData("ToBeLoggedOut","1234", "t@gmail.com");
    private static UserDao givenUserDao;
    private static AuthDao givenAuthDao;


    @BeforeAll
    public static void setUp() {
        givenUserDao = new MemoryUserDao();
        givenAuthDao = new MemoryAuthDao();

        userService = new UserService(givenUserDao, givenAuthDao);
    }

    @AfterEach
    public void cleanUp() throws DataAccessException {
        givenUserDao.clearUser();
        givenAuthDao.clearAuth();
    }

    //Register Positive and Negative Tests
    @Test
    public void registerSuccessTest() throws DataAccessException {
        UserData newUser = new UserData("MyTestUser","Pass123", "t@gmail.com");
        AuthData response = userService.register(newUser);
        Assertions.assertNotNull(response);
    }
    @Test
    public void registerFailTest1() { //empty username
        UserData newUser = new UserData(null,"Pass123", "t@gmail.com");
        DataAccessException expectedError = Assertions.assertThrows(DataAccessException.class, () -> userService.register(newUser));
        Assertions.assertEquals("Error: bad request", expectedError.getMessage());
    }
    @Test
    public void registerFailTest2() { //empty password
        UserData newUser = new UserData("givenUser",null, "t@gmail.com");
        DataAccessException expectedError = Assertions.assertThrows(DataAccessException.class, () -> userService.register(newUser));
        Assertions.assertEquals("Error: bad request", expectedError.getMessage());
    }


    //Logout Positive and Negative Tests
    @Test
    public void logoutSuccessTest() throws DataAccessException {
        AuthData authData = userService.register(testUser);
        Assertions.assertDoesNotThrow(() -> userService.logout(authData.authToken()));
    }
    @Test
    public void logoutFailTest1() throws DataAccessException { //wrong authToken
        userService.register(testUser);
        DataAccessException expectedError = Assertions.assertThrows(DataAccessException.class, () -> userService.logout("fakeAuthToken"));
        Assertions.assertEquals("Error: unauthorized", expectedError.getMessage());
    }
    @Test
    public void logoutFailTest2() { //empty MemoryAuthDAO
        DataAccessException expectedError = Assertions.assertThrows(DataAccessException.class, () -> userService.logout("uselessAuthToken"));
        Assertions.assertEquals("Error: unauthorized", expectedError.getMessage());
    }


    //Login Positive and Negative Tests
    @Test
    public void loginSuccessTest() throws DataAccessException {
        givenAuthDao.createAuth("fakeUser", "2BeRemoved");
        Assertions.assertDoesNotThrow(() -> userService.logout("2BeRemoved"));
    }
    @Test
    public void loginFailTest() throws DataAccessException {
        givenAuthDao.createAuth("fakeUser", "2BeRemoved");
        DataAccessException expectedError = Assertions.assertThrows(DataAccessException.class, () -> userService.logout("fakeAuthToken"));
        Assertions.assertEquals("Error: unauthorized", expectedError.getMessage());
    }


}
