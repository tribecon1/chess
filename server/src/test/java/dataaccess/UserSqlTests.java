package dataaccess;

import dataaccess.dao.AuthDao;
import dataaccess.dao.GameDao;
import dataaccess.dao.UserDao;
import dataaccess.dao.sqldao.SqlAuthDao;
import dataaccess.dao.sqldao.SqlGameDao;
import dataaccess.dao.sqldao.SqlUserDao;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.SystemService;

import java.sql.SQLException;

public class UserSqlTests {

    private final GameDao gameDao = new SqlGameDao();
    private final AuthDao authDao = new SqlAuthDao();
    private final UserDao userDao = new SqlUserDao();

    @BeforeAll
    public static void before() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    @BeforeEach
    void preTest() throws DataAccessException {
        new SystemService(gameDao, authDao, userDao).clear();
    }

    @Test
     void createUserSuccess() throws DataAccessException {
        assert (userDao.getDatabaseSize() == 0);
        Assertions.assertDoesNotThrow(() -> userDao.createUser("uniqueUser","7654", "u@gmail.com"));
        assert (userDao.getDatabaseSize() == 1);
    }

    @Test
    void createUserDuplicateFailure() throws DataAccessException {
        createUserSuccess();
        Assertions.assertThrows(DataAccessException.class, () -> userDao.createUser("uniqueUser","repeated", "faker"));
        assert (userDao.getDatabaseSize() == 1);
    }

    @Test
    void createUserNullPassFailure() throws DataAccessException {
        assert (userDao.getDatabaseSize() == 0);
        Assertions.assertThrows(DataAccessException.class, () -> userDao.createUser("tester",null, "t@gmail.com"));
        assert (userDao.getDatabaseSize() == 0);
    }

    @Test
    void getUserSuccess() throws DataAccessException {
        createUserSuccess();
        UserData foundUser = userDao.getUser("uniqueUser");
        assert (foundUser != null); //can only be null or UserData obj.
        assert (foundUser.password().equals("7654"));
    }

    @Test
    void getUserFailure() throws DataAccessException {
        assert (userDao.getDatabaseSize() == 0);
        assert (userDao.getUser("fakePlayer") == null);
    }

    @Test
    void clearUserTable() throws DataAccessException, SQLException {
        userDao.createUser("dummy1", "pass", "mail");
        userDao.createUser("dummy2", "pass", "mail");
        userDao.createUser("dummy3", "pass", "mail");
        assert (userDao.getDatabaseSize() == 3);
        userDao.clearUser();
        DatabaseManager.createTables();
        assert (userDao.getDatabaseSize() == 0);
    }

    @Test
    void getTableSizeSuccess() throws DataAccessException, SQLException {
        createUserSuccess();
        userDao.createUser("dummy1", "pass", "mail");
        assert (userDao.getDatabaseSize() == 2);
    }

    @Test
    void getTableSizeFailure() throws DataAccessException, SQLException {
        getTableSizeSuccess();
        userDao.clearUser();
        Assertions.assertThrows(DataAccessException.class, userDao::getDatabaseSize);
    }



}
