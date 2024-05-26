package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.GameDao;
import dataaccess.dao.UserDao;
import dataaccess.dao.memoryDao.MemoryAuthDao;
import dataaccess.dao.memoryDao.MemoryGameDao;
import dataaccess.dao.memoryDao.MemoryUserDao;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;

public class SystemServiceTests {

    private static SystemService systemService;

    private static UserDao givenUserDao;
    private static AuthDao givenAuthDao;
    private static GameDao givenGameDao;


    @BeforeAll
    public static void setUp() throws DataAccessException {
        givenUserDao = new MemoryUserDao();
        givenAuthDao = new MemoryAuthDao();
        givenGameDao = new MemoryGameDao();

        UserService userService = new UserService(givenUserDao, givenAuthDao);
        GameService gameService = new GameService(givenGameDao, givenAuthDao);

        for (int i = 0; i < 4; i++){
            AuthData returnedAuth = userService.register(new UserData(Integer.toString(i), "pass", "test@gmail.com" ));
            gameService.createGame(returnedAuth.authToken(), new CreateGameRequest(Integer.toString(i)));
        }
        systemService = new SystemService(givenGameDao, givenAuthDao, givenUserDao);
    }

    @Test
    public void ClearSuccess() throws DataAccessException {
        assert (givenAuthDao.getDatabaseSize() != 0);
        assert (givenUserDao.getDatabaseSize() != 0);
        assert (givenGameDao.getDatabaseSize() != 0);
        systemService.clear();
        assert (givenAuthDao.getDatabaseSize() == 0);
        assert (givenUserDao.getDatabaseSize() == 0);
        assert (givenGameDao.getDatabaseSize() == 0);
    }



}
