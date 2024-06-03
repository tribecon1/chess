package dataaccess;

import dataaccess.dao.AuthDao;
import dataaccess.dao.GameDao;
import dataaccess.dao.UserDao;
import dataaccess.dao.sqldao.SqlAuthDao;
import dataaccess.dao.sqldao.SqlGameDao;
import dataaccess.dao.sqldao.SqlUserDao;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.SystemService;

import java.sql.SQLException;

public class AuthSqlTests {

    private final GameDao gameDao = new SqlGameDao();
    private final AuthDao authDao = new SqlAuthDao();
    private final UserDao userDao = new SqlUserDao();

    @BeforeEach
    void preTest() throws DataAccessException {
        DatabaseManager.createDatabase();
        new SystemService(gameDao, authDao, userDao).clear();
    }

    @Test
    void createAuthSuccess() throws DataAccessException {
        assert (authDao.getDatabaseSize() == 0);
        Assertions.assertEquals(new AuthData("uniqueUser", "bigBadAuthToken"), authDao.createAuth("uniqueUser","bigBadAuthToken"));
        assert (authDao.getDatabaseSize() == 1);
    }

    @Test
    void createSecondAuthSameUserSuccess() throws DataAccessException {
        createAuthSuccess();
        Assertions.assertEquals(new AuthData("uniqueUser", "otherAuth"), authDao.createAuth("uniqueUser","otherAuth"));
        assert (authDao.getDatabaseSize() == 2);
    }

    @Test
    void createAuthFailure() throws DataAccessException {
        assert (authDao.getDatabaseSize() == 0);
        Assertions.assertThrows(DataAccessException.class, () -> authDao.createAuth(null,"bigBadAuthToken"));
        assert (authDao.getDatabaseSize() == 0);
    }

    @Test
    void createAuthFailure2() throws DataAccessException {
        assert (authDao.getDatabaseSize() == 0);
        Assertions.assertThrows(DataAccessException.class, () -> authDao.createAuth("username!",null));
        assert (authDao.getDatabaseSize() == 0);
    }

    @Test
    void getAuthSuccess() throws DataAccessException {
        createAuthSuccess();
        AuthData foundAuth = authDao.getAuth("bigBadAuthToken");
        assert (foundAuth != null);
        assert (foundAuth.username().equals("uniqueUser"));
    }

    @Test
    void getAuthEmptyFailure() throws DataAccessException {
        assert (authDao.getDatabaseSize() == 0);
        assert (authDao.getAuth("fakeAuthToken") == null);
    }

    @Test
    void getAuthWrongTokenFailure() throws DataAccessException {
        createAuthSuccess();
        AuthData foundAuth = authDao.getAuth("nonExistentAuthToken");
        assert (foundAuth == null);
    }

    @Test
    void deleteAuthSuccess() throws DataAccessException {
        createAuthSuccess();
        Assertions.assertDoesNotThrow(()-> authDao.deleteAuth("bigBadAuthToken"));
        assert (authDao.getDatabaseSize() == 0);
    }

    @Test
    void deleteAuthFailure1() throws DataAccessException {
        createAuthSuccess();
        authDao.deleteAuth("wrongAuthToken");
        assert (authDao.getDatabaseSize() == 1);
    }

    @Test
    void deleteAuthFailure2() throws DataAccessException {
        createAuthSuccess();
        authDao.deleteAuth(null);
        assert (authDao.getDatabaseSize() == 1);
    }

    @Test
    void clearAuthTable() throws DataAccessException, SQLException {
        authDao.createAuth("dummy1", "authorized");
        authDao.createAuth("dummy2", "permission");
        authDao.createAuth("dummy3", "allowed");
        assert (authDao.getDatabaseSize() == 3);
        authDao.clearAuth();
        DatabaseManager.createTables();
        assert (userDao.getDatabaseSize() == 0);
    }

    @Test
    void getTableSizeSuccess() throws DataAccessException, SQLException {
        createAuthSuccess();
        authDao.createAuth("dummy1", "permissible");
        assert (authDao.getDatabaseSize() == 2);
    }

    @Test
    void getTableSizeFailure() throws DataAccessException, SQLException {
        getTableSizeSuccess();
        authDao.clearAuth();
        Assertions.assertThrows(DataAccessException.class, authDao::getDatabaseSize);
    }

}
