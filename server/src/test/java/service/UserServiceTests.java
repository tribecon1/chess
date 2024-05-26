package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.UserDao;
import dataaccess.dao.memoryDao.MemoryAuthDao;
import dataaccess.dao.memoryDao.MemoryUserDao;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import response.ErrorResponse;
import response.ResponseType;


public class UserServiceTests {

    private static UserService userService;


    @BeforeAll
    public static void setUp() {
        UserDao givenUserDao = new MemoryUserDao();
        AuthDao givenAuthDao = new MemoryAuthDao();

        userService = new UserService(givenUserDao, givenAuthDao);
    }
    /*
    //Register Positive and Negative Tests
    @Test
    public void registerSuccessTest() throws DataAccessException {
        UserData newUser = new UserData("MyTestUser","Pass123", "t@gmail.com");
        ResponseType response = userService.register(newUser);
        Assertions.assertNotNull(response);
        assert (response instanceof AuthData);
    }
    @Test
    public void registerFailTest1() throws DataAccessException { //empty username
        UserData newUser = new UserData(null,"Pass123", "t@gmail.com");
        ResponseType response = userService.register(newUser);
        Assertions.assertNotNull(response);
        assert (response instanceof ErrorResponse);
        Assertions.assertEquals( ((ErrorResponse) response).message(), "Error: bad request");
    }
    @Test
    public void registerFailTest2() throws DataAccessException { //empty password
        UserData newUser = new UserData("givenUser",null, "t@gmail.com");
        ResponseType response = userService.register(newUser);
        Assertions.assertNotNull(response);
        assert (response instanceof ErrorResponse);
        Assertions.assertEquals( ((ErrorResponse) response).message(), "Error: bad request");
    }


    //Logout Positive and Negative Tests
    @Test
    public void logoutSuccessTest() throws DataAccessException {
        UserData testUser = new UserData("ToBeLoggedOut","1234", "t@gmail.com");
        ResponseType authData = userService.register(testUser);
        //ResponseType response = userService.logout(((AuthData) authData).authToken());
        //Assertions.assertNull(response);
    }
    @Test
    public void logoutFailTest1() throws DataAccessException { //wrong authToken
        UserData testUser = new UserData("ToBeLoggedOut","1234", "t@gmail.com");
        //ResponseType response = userService.logout("fakeAuthToken");
        //assert (response instanceof ErrorResponse);
        //Assertions.assertEquals( ((ErrorResponse) response).message(), "Error: unauthorized");
    }
    @Test
    public void logoutFailTest2() throws DataAccessException { //empty MemoryAuthDAO
        //ResponseType response = userService.logout("uselessAuthToken");
        //assert (response instanceof ErrorResponse);
        //Assertions.assertEquals( ((ErrorResponse) response).message(), "Error: unauthorized");
    }


    //Login Positive and Negative Tests
    @Test
    public void loginSuccessTest() throws DataAccessException {
        AuthDao presetAuthDao = new MemoryAuthDao();
        presetAuthDao.createAuth("fakeUser", "2BeRemoved");
        UserService tempUserService = new UserService(new MemoryUserDao(), presetAuthDao);
        ResponseType response = tempUserService.logout("2BeRemoved");
        Assertions.assertNull(response);
    }
    @Test
    public void loginFailTest() throws DataAccessException {
        UserService tempUserService = new UserService(new MemoryUserDao(), new MemoryAuthDao());
        ResponseType response = tempUserService.logout("nonExistingUser");
        Assertions.assertNotNull(response);
        assert (response instanceof ErrorResponse);
        Assertions.assertEquals( ((ErrorResponse) response).message(), "Error: unauthorized");
    }



*/
}
