package dataaccess.dao.sqldao;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import model.AuthData;

public class SqlAuthDao implements AuthDao {


    @Override
    public AuthData createAuth(String username, String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clearAuth() throws DataAccessException {

    }

    @Override
    public int getDatabaseSize() {
        return 0;
    }
}
