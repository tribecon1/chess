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
import response.ResponseType;


public class UserServiceTests {

    private static UserService userService;


    @BeforeAll
    public static void setUp() {
        UserDao givenUserDao = new MemoryUserDao();
        AuthDao givenAuthDao = new MemoryAuthDao();

        userService = new UserService(givenUserDao, givenAuthDao);
    }


    @Test
    public void registerSuccessTest() throws DataAccessException {
        UserData newUser = new UserData("MyTestUser","Pass123", "t@gmail.com");
        ResponseType response = userService.register(newUser);
        Assertions.assertNotNull(response);
        assert (response instanceof AuthData);

    }





}
