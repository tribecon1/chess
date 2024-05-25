package dataaccess.dao;

import dataaccess.DataAccessException;
import model.AuthData;

public interface AuthDao {

    AuthData createAuth(String username, String authToken) throws DataAccessException;
    //will generate authToken w/in overridden method call
    //necessary to return AuthData object, or just void?

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

    void clearAuth() throws DataAccessException;

}
