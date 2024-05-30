package dataaccess.dao.sqldao;

import dataaccess.DataAccessException;
import dataaccess.dao.UserDao;
import model.UserData;

public class SqlUserDao implements UserDao {


    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearUser() throws DataAccessException {

    }

    @Override
    public int getDatabaseSize() {
        return 0;
    }
}
